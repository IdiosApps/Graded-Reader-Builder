package TeXStyling

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.ArrayList

fun addStyling(inputArray: ArrayList<ArrayList<String>>, outputStoryFilename: String, markupType: String){
    // prepare to replace content in outputStoryFile
    val path = Paths.get(outputStoryFilename)
    val charset = StandardCharsets.UTF_8
    var content = String(Files.readAllBytes(path), charset)

    // add styling to specific words
    inputArray.forEachIndexed { index, inputArrayElement ->
        if (markupType=="underline"){
            content = content.replace(inputArrayElement[0].toRegex(), "\\\\uline{" + inputArrayElement[0] + "}")
        }
        else if (markupType=="superscript"){
            var firstVocabOccurance: Int = Integer.parseInt(inputArrayElement[3]) -1 //-1 because of title page
            content = content.replace(inputArrayElement[0].toRegex(), inputArrayElement[0] + "\\\\textsuperscript{" + firstVocabOccurance + "." + (index+1) + "}")
        }
    }
    Files.write(path, content.toByteArray(charset))
}