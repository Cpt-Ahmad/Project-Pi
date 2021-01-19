package de.ash.xkay.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import de.ash.xkay.ecs.components.AnimationComponent
import de.ash.xkay.ecs.components.GraphicComponent
import de.ash.xkay.ecs.components.RotateComponent
import de.ash.xkay.ecs.components.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.oneOf

/**
 * Updates the sprites of entities to make them animated.
 *
 * @since 0.1
 * @author Cpt-Ash (Ahmad Haidari)
 */
class AnimationSystem : IteratingSystem(
    allOf(GraphicComponent::class)
        .oneOf(AnimationComponent::class, RotateComponent::class)
        .get()
) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val graphics = entity[GraphicComponent.mapper]
        require(graphics != null) { "Entity $entity does not have a GraphicComponent" }

        // Update the animation component
        entity[AnimationComponent.mapper]?.let { animation ->
            if (animation.update(deltaTime)) {
                graphics.setSprite(animation.getCurrentFrame())
            }
        }

        // Rotate the sprite if neccessary
        entity[RotateComponent.mapper]?.let { rotation ->
            graphics.sprite.rotate(rotation.degreesPerSecond * deltaTime)
        }
    }
}