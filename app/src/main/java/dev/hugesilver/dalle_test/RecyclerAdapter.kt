package dev.hugesilver.dalle_test

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dev.hugesilver.dalle_test.databinding.ListItemViewBinding

class RecyclerAdapter(private val context: Context, private val data: ArrayList<String>) :
    RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>() {
    class RecyclerHolder(private val binding: ListItemViewBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindViewHolder(data: String) {
            Glide.with(context)
                .load(data)
                .transform(CenterCrop(), RoundedCorners(20))
                .into(binding.imageview)

            binding.imageview.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                    .setTitle("사진 저장")
                    .setMessage("해당 사진을 저장하시겠습니까?")
                    .setPositiveButton("저장") { dialog, _ ->
                        val downloadManager =
                            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                        val fileName = "${System.currentTimeMillis()}.png"
                        downloadManager.enqueue(
                            DownloadManager.Request(Uri.parse(data))
                                .setTitle(fileName)
                                .setDestinationInExternalPublicDir(
                                    Environment.DIRECTORY_PICTURES,
                                    fileName
                                )
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        )
                        Toast.makeText(context, "이미지를 저장하였습니다.", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                    .setNegativeButton("취소") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()

                builder.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        var binding = ListItemViewBinding.inflate(LayoutInflater.from(context))
        return RecyclerHolder(binding, context)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        holder.bindViewHolder(data[position])

    }

}