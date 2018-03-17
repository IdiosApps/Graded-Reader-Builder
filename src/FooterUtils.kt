package FooterUtils

import Footers
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.collections.ArrayList

fun addVocabFooters(vocabComponentArray: ArrayList<ArrayList<String>>, outputStoryFilename: String, texLinesPDFPageLastSentence: ArrayList<Int>,languageUsed: String, pdfNumberOfPages: Int, texLinesLastSentenceIndex: ArrayList<Int>, pdfPageLastSentences: ArrayList<String>){
    var pageNumber = 2
    val outputStoryFile = File(outputStoryFilename)
    var footers = Footers()

    VocabUtils.getVocabIndicies(vocabComponentArray) // add vocab "order of appearance" index // todo create this automatically.

    var languageMarker = LanguageUtils.prefixLaTeXLanguageMarker(languageUsed)
    val texPath = Paths.get(outputStoryFilename)
    val lines = Files.readAllLines(texPath, StandardCharsets.UTF_8)

    while (pageNumber < pdfNumberOfPages) {
        generateFooters(vocabComponentArray, pageNumber, languageMarker, footers)

        var footerCallText: String = "\\thispagestyle{f"+(pageNumber-1)+"}\\clearpage"

        var lineToChange = lines[texLinesPDFPageLastSentence[pageNumber-2]]

        var lineWithReference = lineToChange.substring(0, texLinesLastSentenceIndex[pageNumber-2] + pdfPageLastSentences[pageNumber-2].length) +
                                footerCallText +
                                lineToChange.substring((texLinesLastSentenceIndex[pageNumber-2] + pdfPageLastSentences[pageNumber-2].length), lineToChange.length)

        lines[texLinesPDFPageLastSentence[pageNumber-2]] = lineWithReference
        pageNumber ++
    }
    addFooterContentSection(outputStoryFilename,footers) // footer references need a list of footer contents
    Files.write(texPath, lines, StandardCharsets.UTF_8)
}

fun generateFooters(vocabComponentArray: ArrayList<ArrayList<String>>, pageNumber: Int,languageMarker: String, footers: Footers){
    // generate a footer for a given pageNumber
    var pagesVocab: ArrayList<ArrayList<String>> = ArrayList<ArrayList<String>>()
    var vocabInFooterIndex = 0
    var leftFooter = StringBuilder()
    var rightFooter = StringBuilder()

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
    footers.lfoots.add(leftFooter.toString())
    footers.rfoots.add(rightFooter.toString())
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
    // do this with Files.readAllLines, or scanning line-by-line?
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