package com.mobilerpgpack.phone.ui.activity

import android.os.Bundle
import com.mobilerpgpack.phone.databinding.MainActivityBinding
import com.mobilerpgpack.phone.presenter.MainActivityPresenter
import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.presenter.InjectPresenter

internal class MainActivity : MvpAppCompatActivity(), MvpView {
    @InjectPresenter
    lateinit var presenter: MainActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startGameButton.setOnClickListener {
            presenter.onStartGameBtnClicked(this@MainActivity)
        }

        presenter.requestExternalStorage(this)
//        changeFragment()
    }

   /* private fun changeFragment() {
        val fragmentContainterResId = R.id.fragments_container
        var fragment = supportFragmentManager.findFragmentById(fragmentContainterResId)

        if (fragment == null) {
            fragment = SettingsFragment()
            supportFragmentManager.beginTransaction().apply {
                replace(fragmentContainterResId, fragment)
                commit()
            }
        }
    }*/
}
