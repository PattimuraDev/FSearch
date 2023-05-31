package repo.pattimuradev.fsearch.misc

import repo.pattimuradev.fsearch.model.Chat

/**
 * Object untuk menampung fungsi custom tentang sorting objek yang ada di aplikasi, e.g chat
 * @author PattimuraDev (Dwi Satria Patra)
 */
object ObjectSorter {
    /**
     * Fungsi untuk mengurutkan chat berdasarkan waktu kirimnya
     * @author PattimuraDev (Dwi Satria Patra)
     * @param listChat list dari chat yang akan diurutkan
     * @return list chat yang sudah diurutkan
     */
    fun chatSorter(listChat: List<Chat>): List<Chat>{
        val listArray = listChat.toTypedArray()
        val size = listArray.size

        for (step in 0 until size-1){
            var minIdx = step
            for(i in (step + 1) until size){
                if(listArray[i].sendingTime < listArray[minIdx].sendingTime){
                    minIdx = i
                }
            }

            val temp = listArray[step]
            listArray[step] = listArray[minIdx]
            listArray[minIdx] = temp
        }

        return listArray.toList()
    }
}