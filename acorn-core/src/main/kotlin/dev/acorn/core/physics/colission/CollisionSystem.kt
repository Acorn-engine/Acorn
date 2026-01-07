package dev.acorn.core.physics.colission

import dev.acorn.core.events.EventBus
import dev.acorn.core.math.Vec2
import dev.acorn.core.math.clamp
import dev.acorn.core.math.times
import dev.acorn.core.physics.BodyType
import dev.acorn.core.physics.collider.BoxCollider
import dev.acorn.core.physics.collider.CircleCollider
import dev.acorn.core.physics.collider.Collider
import dev.acorn.core.physics.event.CollisionEnter
import dev.acorn.core.physics.event.CollisionEvent
import dev.acorn.core.physics.event.CollisionExit
import dev.acorn.core.physics.event.CollisionStay
import dev.acorn.core.scene.GameObject
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

class CollisionSystem {
    val events = EventBus<CollisionEvent>()
    private val activePairs = HashSet<Long>()

    fun step(objects: List<GameObject>) {
        val colliders = ArrayList<Pair<GameObject, Collider>>(objects.size)

        for(go in objects) {
            for(c in go.getComponents(Collider::class.java)) {
                if(c.enabled) colliders += go to c
            }
        }

        val nextPairs = HashSet<Long>()
        for(i in 0 until colliders.size) {
            val (aGo, aCol) = colliders[i]
            for(j in i + 1 until colliders.size) {
                val (bGo, bCol) = colliders[j]
                if(aGo === bGo) continue

                val manifold = intersect(aGo, aCol, bGo, bCol) ?: continue
                val key = pairKey(aCol.id, bCol.id)
                nextPairs += key

                val isNew = key !in activePairs
                if(isNew) {
                    publishEnter(aGo, aCol, bGo, bCol, manifold)
                } else {
                    publishStay(aGo, aCol, bGo, bCol, manifold)
                }

                if(!aCol.isTrigger && !bCol.isTrigger) {
                    resolve(aGo, aCol, bGo, bCol, manifold)
                }
            }
        }

        for(key in activePairs) {
            if(key !in nextPairs) {
                val aID = (key ushr 32).toInt()
                val bID = (key and 0xFFFFFFFFL).toInt()
                publishExitByIDs(colliders, aID, bID)
            }
        }

        activePairs.clear()
        activePairs.addAll(nextPairs)
    }

    private fun publishEnter(aGo: GameObject, a: Collider, bGo: GameObject, b: Collider, m: CollisionManifold) {
        val eA = CollisionEnter(a, b, aGo, bGo, m.normal, m.penetration)
        val eB = CollisionEnter(b, a, bGo, aGo, Vec2(-m.normal.x, -m.normal.y), m.penetration)
        events.publish(eA); events.publish(eB)
        a.events.publish(eA); b.events.publish(eB)
    }

    private fun publishStay(aGo: GameObject, a: Collider, bGo: GameObject, b: Collider, m: CollisionManifold) {
        val eA = CollisionStay(a, b, aGo, bGo, m.normal, m.penetration)
        val eB = CollisionStay(b, a, bGo, aGo, Vec2(-m.normal.x, -m.normal.y), m.penetration)
        events.publish(eA); events.publish(eB)
        a.events.publish(eA); b.events.publish(eB)
    }

    private fun publishExitByIDs(colliders: List<Pair<GameObject, Collider>>, aID: Int, bID: Int) {
        var aGo: GameObject? = null
        var a: Collider? = null
        var bGo: GameObject? = null
        var b: Collider? = null

        for((go, c) in colliders) {
            if(c.id == aID) { aGo = go; a = c }
            else if(c.id == bID) { bGo = go; b = c }
        }

        if(a != null && b != null && aGo != null && bGo != null) {
            val eA = CollisionExit(a, b, aGo, bGo)
            val eB = CollisionExit(b, b, aGo, bGo)
            events.publish(eA); events.publish(eB)
            a.events.publish(eA); b.events.publish(eB)
        }
    }

    private fun resolve(aGo: GameObject, a: Collider, bGo: GameObject, b: Collider, m: CollisionManifold) {
        val wa = if(a.bodyType == BodyType.Static) 0f else 1f
        val wb = if(b.bodyType == BodyType.Static) 0f else 1f
        val total = wa + wb
        if(total <= 0f) return

        val slop = 0.001f
        val percent = 0.8f
        val depth = max(m.penetration - slop, 0f) * percent
        if(depth <= 0f) return

        val correction = m.normal * depth

        if(wa > 0f) {
            aGo.transform.position.x -= correction.x * (wa / total)
            aGo.transform.position.y -= correction.y * (wa / total)
        }

        if(wb > 0f) {
            bGo.transform.position.x -= correction.x * (wb / total)
            bGo.transform.position.y -= correction.y * (wb / total)
        }
    }

    private fun intersect(aGo: GameObject, a: Collider, bGo: GameObject, b: Collider): CollisionManifold? {
        return when(a) {
            is BoxCollider -> when(b) {
                is BoxCollider -> boxBox(aGo, a, bGo, b)
                is CircleCollider -> boxCircle(aGo, a, bGo, b)
                else -> null
            }
            is CircleCollider -> when(b) {
                is CircleCollider -> circleCircle(aGo, a, bGo, b)
                is BoxCollider -> {
                    val m = boxCircle(bGo, b, aGo, a) ?: return null
                    CollisionManifold(Vec2(-m.normal.x, -m.normal.y), m.penetration)
                }
                else -> null
            }
            else -> null
        }
    }

    private fun boxBox(aGo: GameObject, a: BoxCollider, bGo: GameObject, b: BoxCollider): CollisionManifold? {
        val ac = a.worldCenter(aGo)
        val bc = b.worldCenter(bGo)
        val ah = a.halfExtents()
        val bh = b.halfExtents()

        val dx = bc.x - ac.x
        val px = (ah.x + bh.x) - abs(dx)
        if(px <= 0f) return null

        val dy = bc.y - ac.y
        val py = (ah.y + bh.y) - abs(dy)
        if(py <= 0f) return null

        return if(px < py) {
            CollisionManifold(Vec2(if(dx < 0f) -1f else 1f, 0f), px)
        } else {
            CollisionManifold(Vec2(0f, if(dy < 0f) -1f else 1f), py)
        }
    }

    private fun circleCircle(aGo: GameObject, a: CircleCollider, bGo: GameObject, b: CircleCollider): CollisionManifold? {
        val ac = a.worldCenter(aGo)
        val bc = b.worldCenter(bGo)
        val r = a.effectiveRadius() + b.effectiveRadius()

        val d = Vec2(bc.x - ac.x, bc.y - ac.y)
        val dist2 = d.x * d.x + d.y * d.y
        if(dist2 >= r * r) return null

        val dist = sqrt(max(dist2, 0.000001f))
        val n = Vec2(d.x / dist, d.y / dist)
        return CollisionManifold(n, r - dist)
    }

    private fun boxCircle(boxGo: GameObject, box: BoxCollider, circGo: GameObject, circ: CircleCollider): CollisionManifold? {
        val bc = box.worldCenter(boxGo)
        val bh = box.halfExtents()

        val cc = circ.worldCenter(circGo)
        val r = circ.effectiveRadius()

        val closestX = clamp(cc.x, bc.x - bh.x, bc.x + bh.x)
        val closestY = clamp(cc.y, bc.y - bh.y, bc.y + bh.y)

        val dx = cc.x - closestX
        val dy = cc.y - closestY
        val dist2 = dx * dx + dy * dy
        if(dist2 > r * r) return null

        if(dist2 > 0.000001f) {
            val dist = sqrt(dist2)
            val n = Vec2(dx / dist, dy / dist)
            return CollisionManifold(n, r - dist)
        }

        val localX = cc.x - bc.x
        val localY = cc.y - bc.y

        val px = bh.x - abs(localX)
        val py = bh.y - abs(localY)

        return if(px < py) {
            val nx = if(localX < 0f) -1f else 1f
            CollisionManifold(Vec2(nx, 0f), r + px)
        } else {
            val ny = if(localY < 0f) -1f else 1f
            CollisionManifold(Vec2(0f, ny), r + py)
        }
    }

    private fun pairKey(a: Int, b: Int): Long {
        val lo = minOf(a, b)
        val hi = maxOf(a, b)
        return (hi.toLong() shl 32) or (lo.toLong() and 0xFFFFFFFFL)
    }
}