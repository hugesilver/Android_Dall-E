package dev.hugesilver.dalle_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import dev.hugesilver.dalle_test.databinding.ActivityReturnBinding

class ReturnActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityReturnBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.recyclerView.adapter =
            intent.getStringArrayListExtra("images")?.let { RecyclerAdapter(this, it) }
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
    }
}

