package TexUtils

import java.io.File
import java.io.PrintWriter
import java.util.*

fun getTexLineNumbers(outputStoryFilename: String, pdfPageFirstSentences: ArrayList<String>, texLinesPDFPageFirstSentences: ArrayList<Int>) {
    val inputFile = File(outputStoryFilename) // get file ready
    val scan = Scanner(inputFile)
    var pdfPageFirstSentenceIndexer = 0
    var lineCount = 1

    while (scan.hasNextLine()) {
        var line: String = scan.nextLine()
        if (pdfPageFirstSentenceIndexer<pdfPageFirstSentences.size) {
            if (line.contains(pdfPageFirstSentences[pdfPageFirstSentenceIndexer])) {
                texLinesPDFPageFirstSentences.add(lineCount)
                pdfPageFirstSentenceIndexer++
            }
            lineCount++
        }
    }
    scan.close()
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
