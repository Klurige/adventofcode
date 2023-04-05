package y2015.d15

import kotlin.math.max
import kotlin.math.pow

fun day(data: List<String>) {
    val ingr = mutableSetOf<Ingredient>()
    data.forEach { line ->
        ingr.add(Ingredient(line))
    }
    val ingredients = ingr.toTypedArray()

    val combinations = mutableSetOf<IntArray>()
    (0..100.0.pow(ingredients.size.toDouble()).toLong()).forEach {
        var x = 0
        var sum = 0
        ingredients.forEach { ingredient ->
            ingredient.amount += x + 1
            if (ingredient.amount > 100) {
                x = 1
                ingredient.amount -= 100
            }
            sum += ingredient.amount
        }
        if (sum == 100) {
            combinations.add(ingredients.map { ingredient ->
                ingredient.amount
            }.toIntArray())
        }
    }

    var max = Int.MIN_VALUE
    var caloriesmax = Int.MIN_VALUE
    combinations.forEach { amounts ->
        amounts.indices.forEach { index ->
            ingredients[index].amount = amounts[index]
        }

        val capacity = max(ingredients.sumOf { ingredient ->
            ingredient.amount * ingredient.capacity
        }, 0)
        val durability = max(ingredients.sumOf { ingredient ->
            ingredient.amount * ingredient.durability
        }, 0)
        val flavour = max(ingredients.sumOf { ingredient ->
            ingredient.amount * ingredient.flavour
        }, 0)
        val texture = max(ingredients.sumOf { ingredient ->
            ingredient.amount * ingredient.texture
        }, 0)
        val calories = max(ingredients.sumOf { ingredient ->
            ingredient.amount * ingredient.calories
        }, 0)
        val score = capacity * durability * flavour * texture
        if (score > max) max = score
        if (calories == 500) {
            val score = capacity * durability * flavour * texture
            if (score > caloriesmax) caloriesmax = score
        }
    }
    println("Part1: $max")
    println("Part2: $caloriesmax")
}

class Ingredient(
    val name: String,
    val capacity: Int,
    val durability: Int,
    val flavour: Int,
    val texture: Int,
    val calories: Int,
    var amount: Int = 0
) {
    constructor(line: String) : this(
        name = line.split(',')[0].split(':')[0],
        capacity = line.split(',')[0].split(' ').last().toInt(),
        durability = line.split(',')[1].trim().split(' ').last().toInt(),
        flavour = line.split(',')[2].trim().split(' ').last().toInt(),
        texture = line.split(',')[3].trim().split(' ').last().toInt(),
        calories = line.split(',')[4].trim().split(' ').last().toInt()
    )

    override fun toString(): String {
        return name
    }
}