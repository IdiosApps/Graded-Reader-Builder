package TexUtils

import java.io.File
import java.io.PrintWriter
import java.util.*

fun getTexLineNumber(outputStoryFilename: String, pdfPageFirstSentences: ArrayList<String>, texLinesPDFPageFirstSentence: ArrayList<Int>){
    println("pdfPageFirstSentences: " + pdfPageFirstSentences)
    pdfPageFirstSentences.forEachIndexed { index, pdfPageFirstSentence ->
        val inputFile = File(outputStoryFilename) // get file ready
        val scan = Scanner(inputFile)
        var lineCount = 0

        while (scan.hasNextLine()) {                              // TODO stop scanning when the first occurance is met
            val line: String = scan.nextLine()
            if (line.contains(pdfPageFirstSentence)) {
                texLinesPDFPageFirstSentence.add(lineCount+1)
            }
            lineCount+=1
        }
        scan.close()
    }
}

fun copyToTex(outputStoryWriter: PrintWriter, inputFilename: String){
    val inputFile = File(inputFilename)
    val scan = Scanner(inputFile)

    while(scan.hasNextLine()) {
        val line: String = scan.nextLine() // read all lines
        if (line.contains("Chapter")) {   // add chapter markup if dealing with a chapter
            outputStoryWriter.println("\\clearpage")
            outputStoryWriter.println("{\\centering \\large")
            outputStoryWriter.println("{\\uline{" + line + "}}\\\\}")
        }
        else {     // else (for now) assume we have ordinary text
            outputStoryWriter.println(line)
        }
    }
    scan.close()
}