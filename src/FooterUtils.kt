package FooterUtils

import Footers
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

fun addVocabFooters(vocabComponentArray: ArrayList<ArrayList<String>>, outputStoryFilename: String, texLinesPDFPageLastSentence: ArrayList<Int>,languageUsed: String, pdfNumberOfPages: Int){
    var pageNumber = 2
    val outputStoryFile = File(outputStoryFilename)
    var footers = Footers()


    VocabUtils.getVocabIndicies(vocabComponentArray) // add vocab "order of appearance" index // todo create this automatically.

    var languageMarker = LanguageUtils.prefixLaTeXLanguageMarker(languageUsed)
    val texPath = Paths.get(outputStoryFilename)
    val lines = Files.readAllLines(texPath, StandardCharsets.UTF_8)
    var trackTexLinesAdded = 0

    while (pageNumber < pdfNumberOfPages) {
        println("pageNumber, pdfNumberOfPages" + pageNumber + ", " +pdfNumberOfPages)
        var rightFooter = StringBuilder("\\rfoot{ ")
        var leftFooter = StringBuilder("\\lfoot{ ")
        generateFooters(vocabComponentArray, pageNumber, leftFooter, rightFooter, languageMarker)

        // store the footers, and add footer-callers (and page-endings) to the ends of pages
        footers.lfoots.add(leftFooter.toString())
        footers.rfoots.add(rightFooter.toString())
        var footerCallText: String = " \\thispagestyle{f"+pageNumber+"}\\clearpage "
        // TODO add footerCallText specifically at pdf's-last-line[indexOf(pdf's-last-line)+ pdf's-last-line.length]
        lines.add(texLinesPDFPageLastSentence[pageNumber-2]+ 1 + trackTexLinesAdded, footerCallText)
        trackTexLinesAdded ++ // see if this is neccesary. Think it is (for 10 page+ books).
        pageNumber ++
    }
    addFooterContentSection(outputStoryFilename,footers)
    Files.write(texPath, lines, StandardCharsets.UTF_8)
}


fun generateFooters(vocabComponentArray: ArrayList<ArrayList<String>>, pageNumber: Int, leftFooter: StringBuilder, rightFooter: StringBuilder,languageMarker: String){
    // generate a footer for a given pageNumber
    var pagesVocab: ArrayList<ArrayList<String>> = ArrayList<ArrayList<String>>()
    var vocabInFooterIndex = 0

    // store any vocabulary that's on this page, from all the vocab.
    vocabComponentArray.forEachIndexed { index, currentVocab ->
        if(Integer.parseInt(currentVocab[currentVocab.size-2]) == pageNumber){ // last component of CurrentVocab is the index, 2nd from last is page number.
            pagesVocab.add(currentVocab)
        }
    }

    // two "if {while/while}" sections deal out vocab to left/right footers
    if ((pagesVocab.size % 2)==0) {    // left/right footers have 50/50 vocab
        while (vocabInFooterIndex<(pagesVocab.size/2)){ // left (even)
            appendFooterParts (leftFooter,pagesVocab, vocabInFooterIndex, languageMarker)
            vocabInFooterIndex ++
        }
        while (vocabInFooterIndex<(pagesVocab.size)){  // right (even)
            appendFooterParts (rightFooter,pagesVocab, vocabInFooterIndex, languageMarker)
            vocabInFooterIndex ++
        }
    }
    else {  // left footer has an extra vocab
        while (vocabInFooterIndex<(((pagesVocab.size)+1)/2)){ // left (odd)
            appendFooterParts (leftFooter,pagesVocab, vocabInFooterIndex, languageMarker)
            vocabInFooterIndex ++
        }
        while (vocabInFooterIndex<(pagesVocab.size)){ // right (odd)
            appendFooterParts (rightFooter,pagesVocab, vocabInFooterIndex, languageMarker)
            vocabInFooterIndex ++
        }
    }
    leftFooter.append("}")
    rightFooter.append("}")
}

// check how many parts are provided for vocabulary, then pass to appendFooter2/3+Parts
fun appendFooterParts (footerToBuild: StringBuilder, pagesVocab: ArrayList<ArrayList<String>>, vocabInFooterIndex: Int, languageMarker: String){
    if (pagesVocab[0].size == 4) {
        appendFooterTwoPartsHanEn(footerToBuild, pagesVocab, vocabInFooterIndex, languageMarker)
    }
        else if (pagesVocab[0].size == 5 ) {
        appendFooterThreePartsHanPinEn (footerToBuild, pagesVocab, vocabInFooterIndex, languageMarker)
    }
}

// First add the vocabulary index, then e.g. hanzi then english
fun appendFooterTwoPartsHanEn (footerToBuild: StringBuilder,pagesVocab: ArrayList<ArrayList<String>> ,vocabInFooterIndex: Int, languageMarker: String) {
    footerToBuild.append(Integer.parseInt(pagesVocab[vocabInFooterIndex][pagesVocab[0].size-1]) +1).append(". ").append(pagesVocab[vocabInFooterIndex][0]).append(" ").append(pagesVocab[vocabInFooterIndex][1]).append("\\\\ ")
}
// First add the vocabulary index, then e.g. hanzi (0), then pinyin (1) then english (2)
fun appendFooterThreePartsHanPinEn (footerToBuild: StringBuilder,pagesVocab: ArrayList<ArrayList<String>> ,vocabInFooterIndex: Int, languageMarker: String) {
    footerToBuild.append(Integer.parseInt(pagesVocab[vocabInFooterIndex][pagesVocab[0].size-1]) +1).append(". ").append(pagesVocab[vocabInFooterIndex][0]).append(" ").append(languageMarker).append(pagesVocab[vocabInFooterIndex][1]).append("}) ").append(pagesVocab[vocabInFooterIndex][2]).append("\\\\ ")
}

fun addFooterContentSection(outputStoryFilename: String, footers: Footers) {
    val texPath = Paths.get(outputStoryFilename)
    val lines = Files.readAllLines(texPath, StandardCharsets.UTF_8)
    var beginIndex = lines.indexOf("% Begin Document")
    var footersAddedIndex = 0

    while (footersAddedIndex < footers.lfoots.size) {
        if (footersAddedIndex==0){
            lines.add(beginIndex-1, "% % Footer 1 % %)")
            lines.add(beginIndex-1,"\\fancypagestyle{f1}{")
            lines.add(beginIndex-1,"\\fancyhf{}")
            lines.add(beginIndex-1,"\\renewcommand{\\headrulewidth}{0pt}") // only have the \renewcommand in the first footer's contents.
            lines.add(beginIndex-1,footers.lfoots[footersAddedIndex])
            lines.add(beginIndex-1,footers.rfoots[footersAddedIndex])
        }
        else {
            lines.add(beginIndex-1, "% % Footer "+footersAddedIndex+2+"% %)")
            lines.add(beginIndex-1,"\\fancypagestyle{f"+footersAddedIndex+2+"}{")
            lines.add(beginIndex-1,"\\fancyhf{}")
            lines.add(beginIndex-1,footers.lfoots[footersAddedIndex])
            lines.add(beginIndex-1,footers.rfoots[footersAddedIndex])
        }
        footersAddedIndex++
    }
}