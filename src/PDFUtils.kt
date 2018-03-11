package PDFUtils

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import java.util.*

fun xelatexToPDF (outputStoryFilename: String){
        val process = Runtime.getRuntime().exec("cmd /c start /wait buildPDF.sh")
        val exitVal = process.waitFor()
    }

fun getNumberOfPDFPages (PDFFilename: String, pdfNumberOfPages: Int) : Int {
    val pdfFile = File(PDFFilename)
    val documentPDF: PDDocument = PDDocument.load(pdfFile)
    var pdfNumberOfPages = documentPDF.getNumberOfPages()
    return pdfNumberOfPages
}

// TODO split this into two functions: one for vocab pages, one for first sentences on pages.
fun readPDF (PDFFilename: String, vocabComponentArray: ArrayList<ArrayList<String>>, pdfPageFirstSentences: ArrayList<String>, pdfNumberOfPages: Int){
    val pdfFile = File(PDFFilename)
    val documentPDF: PDDocument = PDDocument.load(pdfFile)

    // Find the first instance of each vocabulary word
    try {
        vocabComponentArray.forEachIndexed { index, currentVocabComponent ->
            var pageCounter = 1 // start at page 1 for each vocab Hanzi
            var pdfPageText = ""

            while(!pdfPageText.contains(currentVocabComponent[0])) {
                val stripper = PDFTextStripper()
                stripper.startPage = pageCounter
                stripper.endPage = pageCounter
                pdfPageText = stripper.getText(documentPDF)

                if (pdfPageText.contains(currentVocabComponent[0])){
                    currentVocabComponent.add(Integer.toString(pageCounter))
                }
                pageCounter +=1
            }
        }
    }
    catch(e: Exception){    }

    // Get the first sentence of each page, and save to array
    try {
        var pdfPageText = ""
        var pageCounter = 2 // start where the story starts (accounting for title page)
        while (pageCounter<pdfNumberOfPages) { // for each page
            val stripper = PDFTextStripper()
            var pdfPageFirstLine = ""
            stripper.startPage = pageCounter
            stripper.endPage = pageCounter
            pdfPageText = stripper.getText(documentPDF)

            var pdfPageTextLines: List<String> = pdfPageText.split("\r\n") //   \r   vs   \n   vs   \r\n    ..?

            pdfPageFirstLine = fixPDFPageFirstLine(pdfPageTextLines[0])
            pdfPageFirstSentences.add(pdfPageFirstLine) // todo improve efficiency; only need 1 (of maybe 20 lines)
            pageCounter +=1
        }
    }
    catch(e: Exception){}
    documentPDF.close()
}

fun fixPDFPageFirstLine (pdfPageFirstTextLine: String): String {
    // Convert string to charArray (easier manipulation; String is immutable)
    var lineAsChars = pdfPageFirstTextLine.toCharArray()

    // Scan through for 8217 and turn it into 39'
    // note: a 39' was converted to 25,32 (2 chars) at first. later just 8217
    // This fixes ' being misread
    var characterIndex = 0
    while (characterIndex < lineAsChars.size) {

        if (lineAsChars[characterIndex].toInt() == 8217) {
            lineAsChars[characterIndex] = 39.toChar()
        }
        characterIndex++
    }

    // Then just scan through and remove the 0s
    // this fixes an ascii/unicode mix-up (I think).
    // this stopped appearing the same time 25,32->8217. Not sure why.
    var cleanedString: StringBuilder = StringBuilder()
    var characterIndexForZeroes = 0

    while (characterIndexForZeroes < lineAsChars.size) {
        if (lineAsChars[characterIndexForZeroes].toInt() == 0) {
        } // don't do anything if the char has Int value of 0
        else { // add every character to stringbuilder
            cleanedString.append(lineAsChars[characterIndexForZeroes])
        }
        characterIndexForZeroes++
    }
    return cleanedString.toString()
}
