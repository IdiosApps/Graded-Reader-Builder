package TexUtils

import java.io.File
import java.io.PrintWriter
import java.util.*

fun getTexLineNumber(outputStoryFilename: String, pdfPageFirstSentences: ArrayList<String>, texLinesPDFPageFirstSentence: ArrayList<Int>) {
    val inputFile = File(outputStoryFilename) // get file ready
    val scan = Scanner(inputFile)
    var pdfPageFirstSentenceIndexer = 0
    var lineCount = 1

    //  TODO figure out why line.contains is failing to find a match for the 2nd page, when it really seems like there should be a match!
    while (scan.hasNextLine()) {
        var line: String = scan.nextLine()
        if (line.contains(pdfPageFirstSentences[pdfPageFirstSentenceIndexer])) {
            texLinesPDFPageFirstSentence.add(lineCount)
            pdfPageFirstSentenceIndexer++
        }
        if (line.contains("code (on Github). It's that simple.")){
            println("at line " + lineCount)
            println("Line:" + line)
        }
        if (lineCount == 96){
            println("pdfPageFirstSentences[pdfPageFirstSentenceIndexer]: " + pdfPageFirstSentences[pdfPageFirstSentenceIndexer])
        }
        lineCount++
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