package uz.gita.bookapp.presentation.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.bookapp.R
import uz.gita.bookapp.databinding.ScreenSplashBinding
import uz.gita.bookapp.presentation.viewmodel.splash.SplashVM
import uz.gita.bookapp.presentation.viewmodel.splash.SplashVMImpl

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreen : Fragment(R.layout.screen_splash) {

    private val viewBinding by viewBinding(ScreenSplashBinding::bind)
    private val viewModel: SplashVM by viewModels<SplashVMImpl>()
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }

    override fun onCreate(savedInstanceState: Bundle?) = with(viewModel) {
        super.onCreate(savedInstanceState)
        versionLD.observe(this@SplashScreen) {
            viewBinding.textVersion.text = resources.getString(R.string.text_splash_version, it)
        }
        navigateBooksScreeLD.observe(this@SplashScreen) {
            navController.navigate(R.id.action_splashScreen_to_booksScreen)
        }
        navigateIntroScreenLD.observe(this@SplashScreen) {
            navController.navigate(R.id.action_splashScreen_to_introScreen)
        }
        uiModeStatusLD.observe(this@SplashScreen, uiModeStatusLDObserver)
    }

    private val uiModeStatusLDObserver = Observer<Boolean> { status ->
        when (status) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}