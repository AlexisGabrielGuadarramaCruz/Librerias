package com.example.librerias

/** Gastos de un mes agrupados por categoría (montos en pesos). */
data class MonthExpenses(
    val month: String,
    val byCategory: MutableMap<String, Float>
) {
    val total: Float get() = byCategory.values.sum()
}

/** Datos de ejemplo para el dashboard. */
object SampleData {
    val months: List<MonthExpenses> = listOf(
        MonthExpenses("Jun", mutableMapOf("Comida" to 3200f, "Transporte" to 900f, "Ocio" to 1100f, "Servicios" to 1800f, "Salud" to 400f)),
        MonthExpenses("Jul", mutableMapOf("Comida" to 3500f, "Transporte" to 1000f, "Ocio" to 1500f, "Servicios" to 1800f, "Salud" to 600f)),
        MonthExpenses("Ago", mutableMapOf("Comida" to 3000f, "Transporte" to 850f, "Ocio" to 900f, "Servicios" to 1900f, "Salud" to 300f)),
        MonthExpenses("Sep", mutableMapOf("Comida" to 3800f, "Transporte" to 1100f, "Ocio" to 2000f, "Servicios" to 1850f, "Salud" to 500f)),
        MonthExpenses("Oct", mutableMapOf("Comida" to 3400f, "Transporte" to 950f, "Ocio" to 1300f, "Servicios" to 1900f, "Salud" to 700f)),
        MonthExpenses("Nov", mutableMapOf("Comida" to 3600f, "Transporte" to 1050f, "Ocio" to 1700f, "Servicios" to 1950f, "Salud" to 450f))
    )
}