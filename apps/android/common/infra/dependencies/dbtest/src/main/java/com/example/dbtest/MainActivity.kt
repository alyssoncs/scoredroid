package com.example.dbtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.dbtest.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import org.scoredroid.infra.dataaccess.dao.InsertMatchDaoRequestModel
import org.scoredroid.infra.dataaccess.dao.MatchDao
import org.scoredroid.infra.dataaccess.database.MatchDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var dao: MatchDao
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Room.databaseBuilder(
            applicationContext,
            MatchDatabase::class.java,
            "the-database"
        ).build()

        dao = db.matchDao()
        binding.button.setOnClickListener {

            val name = binding.matchName.text.toString()
            lifecycleScope.launch {
                dao.insertMatch(InsertMatchDaoRequestModel(name))
            }
        }
    }
}