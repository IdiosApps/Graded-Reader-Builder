package Footers

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

// TODO tidy up, and get it working properly!

fun addVocabFooters(vocabComponentArray: ArrayList<ArrayList<String>>, outputStoryFilename: String, texLinesPDFPageFirstSentence: ArrayList<Int>){
    var pageNumber = 2
    val outputStoryFile = File(outputStoryFilename)
    var rightFooter = StringBuilder("\\rfoot{ ")// .append(c).append(d) // .toString()
    var leftFooter = StringBuilder("\\lfoot{ ")// .append(c).append(d) // .toString()

    VocabUtils.getVocabIndicies(vocabComponentArray) // add vocab "order of appearance" index

    generateFooters(vocabComponentArray,pageNumber,leftFooter,rightFooter)
    println("leftFooter: " + leftFooter)
    println("rightFooter: " + rightFooter)

    texLinesPDFPageFirstSentence.forEachIndexed { index, texLineForPageBegin ->
        val texPath = Paths.get(outputStoryFilename)
        val lines = Files.readAllLines(texPath, StandardCharsets.UTF_8)
        lines.add(texLineForPageBegin + 1, leftFooter.toString())
        lines.add(texLineForPageBegin + 2, rightFooter.toString())
        Files.write(texPath, lines, StandardCharsets.UTF_8)
    }
}

fun generateFooters(vocabComponentArray: ArrayList<ArrayList<String>>, pageNumber: Int, leftFooter: StringBuilder, rightFooter: StringBuilder){
    var pagesVocab: ArrayList<ArrayList<String>> = ArrayList<ArrayList<String>>()
    var FooterCounter = 0

    // get vocab used in current page
    vocabComponentArray.forEachIndexed { index, currentVocab ->
        if(Integer.parseInt(currentVocab[3]) ==pageNumber){
            pagesVocab.add(currentVocab)
        }
    }

    // example of TeX footers:
    //    \lfoot{	x. 对她说 (\pinyin{dui4ta1shuo1}) said to her\\ 	x. 啊 (\pinyin{a1}) who knows..\\ 	x. 聪明 (\pinyin{cong1ming}) intelligent\\ }
    //    \rfoot{ x. 比如 (\pinyin{bi3ru2}) for example\\        x. 再问 (\pinyin{zai4wen4}) ask again\\	x. 谁知道 (\pinyin{shei2zhi1dao}） who knows..?\\ }
    if ((pagesVocab.size % 2)==0) {   // even number of vocab on page
        while (FooterCounter<(pagesVocab.size/2)){
            leftFooter.append(Integer.parseInt(pagesVocab[FooterCounter][4]) +1).append(". ").append(pagesVocab[FooterCounter][0]).append(" (\\pinyin{").append(pagesVocab[FooterCounter][1]).append("}) ").append(pagesVocab[FooterCounter][2]).append("\\\\ ")
            FooterCounter+=1
        }
        while (FooterCounter<(pagesVocab.size)){
            rightFooter.append(Integer.parseInt(pagesVocab[FooterCounter][4]) +1).append(". ").append(pagesVocab[FooterCounter][0]).append(" (\\pinyin{").append(pagesVocab[FooterCounter][1]).append("}) ").append(pagesVocab[FooterCounter][2]).append("\\\\ ")
            FooterCounter+=1
        }
    }
    else {  // odd number of vocab on page
        while (FooterCounter<(((pagesVocab.size)+1)/2)){ // e.g. 2 left, 1 right
            leftFooter.append(Integer.parseInt(pagesVocab[FooterCounter][4]) +1).append(". ").append(pagesVocab[FooterCounter][0]).append(" (\\pinyin{").append(pagesVocab[FooterCounter][1]).append("}) ").append(pagesVocab[FooterCounter][2]).append("\\\\ ")
            FooterCounter+=1
        }
        while (FooterCounter<(pagesVocab.size)){ // rfoot takes what's left of the page's vocab
            rightFooter.append(Integer.parseInt(pagesVocab[FooterCounter][4]) +1).append(". ").append(pagesVocab[FooterCounter][0]).append(" (\\pinyin{").append(pagesVocab[FooterCounter][1]).append("}) ").append(pagesVocab[FooterCounter][2]).append("\\\\ ")
            FooterCounter+=1
        }
    }
    leftFooter.append("}")
    rightFooter.append("}")
}



