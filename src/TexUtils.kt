package TexUtils

import java.io.File
import java.io.PrintWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

fun getTexLineNumbers(outputStoryFilename: String, pdfPageLastSentences: ArrayList<String>, texLinesPDFPageLastSentences: ArrayList<Int>, texLinesLastSentenceIndex: ArrayList<Int>) {
    val inputFile = File(outputStoryFilename) // get file ready
    val scan = Scanner(inputFile)
    var pdfPageLastSentenceIndexer = 0
    var lineCount = 0

    while (scan.hasNextLine()) {
        var line: String = scan.nextLine()
        if (pdfPageLastSentenceIndexer<pdfPageLastSentences.size) {
            if (line.contains(pdfPageLastSentences[pdfPageLastSentenceIndexer])) {
                texLinesPDFPageLastSentences.add(lineCount)
                texLinesLastSentenceIndex.add(line.lastIndexOf(pdfPageLastSentences[pdfPageLastSentenceIndexer]))
                pdfPageLastSentenceIndexer++
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
