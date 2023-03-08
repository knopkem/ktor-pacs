package com.pacsnode.dicom

import com.pixelmed.dicom.*
import com.pixelmed.display.SourceImage
import java.io.File
import java.io.IOException

object ReadMetaDataPixelMed {
    private val attributeList: AttributeList = AttributeList()
    @JvmStatic
    fun printAttributes(dcmFilePath: String) {
        try {
            readAttributes(dcmFilePath)
            val metaData = readMetadata()
            metaData.forEach { entry ->
                print("${entry.key} : ${entry.value}")
            }
        } catch (e: Exception) {
            println("Error due to: " + e.message)
        }
    }

    @Throws(DicomException::class, IOException::class)
    private fun readAttributes(dcmFilePath: String) {
        attributeList.read(File(dcmFilePath))
    }

    @Throws(DicomException::class)
    private fun readMetadata(): Map<String, String> {
        val metaData: MutableMap<String, String> = LinkedHashMap()
        metaData["Patient Name"] = getTagInformation(TagFromName.PatientName)
        metaData["Patient ID"] = getTagInformation(TagFromName.PatientID)
        metaData["Transfer Syntax"] = getTagInformation(TagFromName.TransferSyntaxUID)
        metaData["SOP Class"] = getTagInformation(TagFromName.SOPClassUID)
        metaData["Modality"] = getTagInformation(TagFromName.Modality)
        metaData["Samples Per Pixel"] = getTagInformation(TagFromName.SamplesPerPixel)
        metaData["Photometric Interpretation"] = getTagInformation(TagFromName.PhotometricInterpretation)
        metaData["Pixel Spacing"] = getTagInformation(TagFromName.PixelSpacing)
        metaData["Bits Allocated"] = getTagInformation(TagFromName.BitsAllocated)
        metaData["Bits Stored"] = getTagInformation(TagFromName.BitsStored)
        metaData["High Bit"] = getTagInformation(TagFromName.HighBit)
        val img = SourceImage(attributeList)
        metaData["Number of frames"] = java.lang.String.valueOf(img.numberOfFrames)
        metaData["Width"] = java.lang.String.valueOf(img.width)
        metaData["Height"] = java.lang.String.valueOf(img.height)
        metaData["Is Grayscale"] = java.lang.String.valueOf(img.isGrayscale)
        metaData["Pixel Data present"] = getTagInformation(TagFromName.PixelData).isEmpty().toString()
        return metaData
    }

    private fun getTagInformation(tag: AttributeTag): String {
        return Attribute.getDelimitedStringValuesOrDefault(attributeList, tag, "NOT FOUND")
    }
}