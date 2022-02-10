package com.bugs.posisi_in_1301.map

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class MapView(context: Context, attrs: AttributeSet): View(context, attrs) {
    private val COLS = 10
    private val ROWS = 22
    lateinit var cells: Array<Array<Cell>>
    private val WALL = 2F
    private val wallPaint = Paint()

    init {
        wallPaint.color = Color.BLACK
        wallPaint.strokeWidth = WALL
        createMap()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cellSize = width/(COLS+3)

        val hMargin = (width - COLS*cellSize)/2
        val vMargin = (height - ROWS*cellSize)/2

        canvas.translate(hMargin.toFloat(), vMargin.toFloat())

        for (x in 0 until COLS) {
            for (y in 0 until ROWS) {
                if (cells[x][y].top)
                    canvas.drawLine(
                        x*cellSize.toFloat(),
                        y*cellSize.toFloat(),
                        (x+1)*cellSize.toFloat(),
                        y*cellSize.toFloat(),
                        wallPaint
                    )

                if (cells[x][y].left)
                    canvas.drawLine(
                        x*cellSize.toFloat(),
                        y*cellSize.toFloat(),
                        x*cellSize.toFloat(),
                        (y+1)*cellSize.toFloat(),
                        wallPaint
                    )

                if (cells[x][y].bottom)
                    canvas.drawLine(
                        x*cellSize.toFloat(),
                        (y+1)*cellSize.toFloat(),
                        (x+1)*cellSize.toFloat(),
                        (y+1)*cellSize.toFloat(),
                        wallPaint
                    )

                if (cells[x][y].right)
                    canvas.drawLine(
                        (x+1)*cellSize.toFloat(),
                        y*cellSize.toFloat(),
                        (x+1)*cellSize.toFloat(),
                        (y+1)*cellSize.toFloat(),
                        wallPaint
                    )
            }
        }
    }

    private fun createMap() {

        cells = Array(COLS) { Array(ROWS) { Cell() } }

        for (x in 0 until COLS) {
            for (y in 0 until ROWS) {
                cells[x][y].x = x
                cells[x][y].y = y
            }
        }

        // wall edge top
        for (x in 0 until COLS) {
            val y = 0
            cells[x][y].top = true
        }

        // wall edge left
        for (y in 0 until ROWS) {
            val x = 0
            cells[x][y].left = true
        }

        // wall edge bottom
        for (x in 0 until COLS) {
            val y = ROWS-1
            cells[x][y].bottom = true
        }

        // wall edge right
        for (y in 0 until ROWS) {
            val x = COLS-1
            cells[x][y].right = true
        }

        // ruang 306
        cells[3][0].right = true
        cells[3][1].right = true
        cells[3][3].right = true

        cells[4][0].left = true
        cells[4][1].left = true
        cells[4][3].left = true

        cells[0][3].bottom = true
        cells[1][3].bottom = true
        cells[2][3].bottom = true
        cells[3][3].bottom = true

        // toilet
        cells[6][0].bottom = true
        cells[7][0].bottom = true
        cells[8][0].bottom = true
        cells[9][0].bottom = true

        cells[6][1].top = true
        cells[7][1].top = true
        cells[8][1].top = true
        cells[9][1].top = true

        cells[6][1].bottom = true
        cells[7][1].bottom = true
        cells[8][1].bottom = true
        cells[9][1].bottom = true

        // ruang 302
        cells[6][2].top = true
        cells[7][2].top = true
        cells[8][2].top = true
        cells[9][2].top = true

        cells[6][2].left = true
        cells[6][3].left = true
        cells[6][5].left = true

        cells[6][5].bottom = true
        cells[7][5].bottom = true
        cells[8][5].bottom = true
        cells[9][5].bottom = true

        // ruang 307
        cells[0][4].top = true
        cells[1][4].top = true
        cells[2][4].top = true
        cells[3][4].top = true

        cells[3][4].right = true
        cells[3][5].right = true
        cells[3][7].right = true

        cells[0][7].bottom = true
        cells[1][7].bottom = true
        cells[2][7].bottom = true
        cells[3][7].bottom = true

        // ruang 303
        cells[6][6].top = true
        cells[7][6].top = true
        cells[8][6].top = true
        cells[9][6].top = true

        cells[6][6].left = true
        cells[6][7].left = true
        cells[6][9].left = true

        cells[6][9].bottom = true
        cells[7][9].bottom = true
        cells[8][9].bottom = true
        cells[9][9].bottom = true

        // tangga lor
        cells[0][10].top = true
        cells[1][10].top = true
        cells[2][10].top = true
        cells[3][10].top = true

        cells[0][10].bottom = true
        cells[1][10].bottom = true
        cells[2][10].bottom = true
        cells[3][10].bottom = true

        cells[3][10].right = true

        // ruang 304
        cells[6][10].top = true
        cells[7][10].top = true
        cells[8][10].top = true
        cells[9][10].top = true

        cells[6][10].left = true
        cells[6][11].left = true
        cells[6][13].left = true

        cells[6][13].bottom = true
        cells[7][13].bottom = true
        cells[8][13].bottom = true
        cells[9][13].bottom = true

        // tangga kidul
        cells[0][14].top = true
        cells[1][14].top = true
        cells[2][14].top = true
        cells[3][14].top = true

        cells[0][14].bottom = true
        cells[1][14].bottom = true
        cells[2][14].bottom = true
        cells[3][14].bottom = true

        cells[3][14].right = true

        // ruang 305
        cells[6][14].top = true
        cells[7][14].top = true
        cells[8][14].top = true
        cells[9][14].top = true

        cells[6][14].left = true
        cells[6][15].left = true
        cells[6][17].left = true

        cells[6][17].bottom = true
        cells[7][17].bottom = true
        cells[8][17].bottom = true
        cells[9][17].bottom = true

        // aulo lor
        cells[0][18].top = true
        cells[1][18].top = true
        cells[2][18].top = true
        cells[3][18].top = true

        cells[6][18].top = true
        cells[7][18].top = true
        cells[8][18].top = true
        cells[9][18].top = true

        // dalan kosong lor kidul
        // kulon
        cells[4][0].left = true
        cells[4][1].left = true
        cells[4][3].left = true

        cells[4][4].left = true
        cells[4][5].left = true
        cells[4][7].left = true

        //etan
        cells[5][2].right = true
        cells[5][3].right = true
        cells[5][5].right = true

        cells[5][6].right = true
        cells[5][7].right = true
        cells[5][9].right = true

        cells[5][10].right = true
        cells[5][11].right = true
        cells[5][13].right = true

        cells[5][14].right = true
        cells[5][15].right = true
        cells[5][17].right = true

        // lobi a
        //lor
        cells[0][8].top = true
        cells[1][8].top = true
        cells[2][8].top = true
        cells[3][8].top = true

        //kidul
        cells[0][9].bottom = true
        cells[1][9].bottom = true
        cells[2][9].bottom = true
        cells[3][9].bottom = true

        //lobi b
        // lor
        cells[0][11].top = true
        cells[1][11].top = true
        cells[2][11].top = true
        cells[3][11].top = true

        // kidul
        cells[0][13].bottom = true
        cells[1][13].bottom = true
        cells[2][13].bottom = true
        cells[3][13].bottom = true

        //lobi c
        // lor
        cells[0][15].top = true
        cells[1][15].top = true
        cells[2][15].top = true
        cells[3][15].top = true

        // kidul
        cells[0][17].bottom = true
        cells[1][17].bottom = true
        cells[2][17].bottom = true
        cells[3][17].bottom = true

    }
}