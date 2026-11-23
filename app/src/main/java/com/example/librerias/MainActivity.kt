package com.example.librerias

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.librerias.databinding.ActivityMainBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val months = SampleData.months
    private var selectedIndex = months.lastIndex // por defecto, el mes más reciente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBarChart()
        setupPieChart()
        refreshDashboard()

        // Agrega un gasto aleatorio al mes seleccionado y redibuja ambas gráficas
        binding.btnAddExpense.setOnClickListener {
            val month = months[selectedIndex]
            val category = month.byCategory.keys.random()
            month.byCategory[category] = (month.byCategory[category] ?: 0f) + Random.nextInt(100, 800)
            refreshDashboard()
        }
    }

    // ---------- Configuración inicial ----------

    private fun setupBarChart() = with(binding.barChart) {
        description.isEnabled = false
        legend.isEnabled = false
        axisRight.isEnabled = false
        axisLeft.axisMinimum = 0f
        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            setDrawGridLines(false)
            valueFormatter = IndexAxisValueFormatter(months.map { it.month })
        }
        setFitBars(true)
        setScaleEnabled(false)

        // Al tocar una barra se cambia el mes que muestra el pastel
        setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                e?.let {
                    selectedIndex = it.x.toInt()
                    refreshDashboard()
                }
            }
            override fun onNothingSelected() = Unit
        })
    }

    private fun setupPieChart() = with(binding.pieChart) {
        description.isEnabled = false
        setUsePercentValues(true)
        isDrawHoleEnabled = true
        holeRadius = 45f
        setEntryLabelColor(Color.DKGRAY)
        setEntryLabelTextSize(11f)
        legend.isWordWrapEnabled = true
    }

    // ---------- Actualización de datos ----------

    private fun refreshDashboard() {
        val current = months[selectedIndex]
        binding.tvTotal.text = "Mes: ${current.month} · Total: $%,.0f".format(current.total)
        updateBarChart()
        updatePieChart(current)
    }

    private fun updateBarChart() = with(binding.barChart) {
        val entries = months.mapIndexed { i, m -> BarEntry(i.toFloat(), m.total) }
        val set = BarDataSet(entries, "Gasto mensual").apply {
            // La barra del mes seleccionado se resalta
            setColors(months.indices.map { if (it == selectedIndex) Color.parseColor("#1B5E20") else Color.parseColor("#A5D6A7") })
            valueTextSize = 10f
        }
        data = BarData(set).apply { barWidth = 0.6f }
        animateY(600)
        invalidate()
    }

    private fun updatePieChart(month: MonthExpenses) = with(binding.pieChart) {
        val entries = month.byCategory.map { (category, amount) -> PieEntry(amount, category) }
        val set = PieDataSet(entries, "").apply {
            setColors(ColorTemplate.MATERIAL_COLORS.toList() + ColorTemplate.VORDIPLOM_COLORS.toList())
            sliceSpace = 2f
            valueTextSize = 12f
            valueTextColor = Color.WHITE
            valueFormatter = PercentFormatter(this@with)
        }
        data = PieData(set)
        centerText = month.month
        setCenterTextSize(18f)
        animateY(700)
        invalidate()
    }
}