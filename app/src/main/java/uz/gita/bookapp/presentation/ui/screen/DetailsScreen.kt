package uz.gita.bookapp.presentation.ui.screen

import android.Manifest
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import uz.gita.bookapp.R
import uz.gita.bookapp.data.model.BookData
import uz.gita.bookapp.databinding.ScreenDetailsBinding
import uz.gita.bookapp.presentation.viewmodel.details.DetailsVM
import uz.gita.bookapp.presentation.viewmodel.details.DetailsVMImpl
import uz.gita.bookapp.utils.checkPermissions
import uz.gita.bookapp.utils.snackErrorMessage

@AndroidEntryPoint
class DetailsScreen : Fragment(R.layout.screen_details) {

    private val viewBinding by viewBinding(ScreenDetailsBinding::bind)
    private val viewModel: DetailsVM by viewModels<DetailsVMImpl>()
    private val navArgs: DetailsScreenArgs by navArgs()

    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.apply {
            loadData(navArgs.bookId)
            bookDataLD.observe(this@DetailsScreen, bookDataLDObserver)
            showPermissionLD.observe(this@DetailsScreen, showPermissionLDObserver)
            popBackStackLD.observe(this@DetailsScreen) { navController.popBackStack() }
            navigateReadScreenLD.observe(this@DetailsScreen) { bookId ->
                navController.navigate(
                    DetailsScreenDirections.actionDetailsScreenToReadScreen(bookId)
                )
            }
            errorMessageLD.observe(this@DetailsScreen) { snackErrorMessage(resources.getString(it)) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscribeViewBinding(viewBinding)
        subscribeViewModel(viewModel)
    }

    private fun subscribeViewBinding(viewBinding: ScreenDetailsBinding) = with(viewBinding) {
        textToolbar.isSelected = true
        buttonBack.setOnClickListener { viewModel.onClickBackButton() }
        buttonRead.setOnClickListener { viewModel.onClickReadBook() }

    }

    private fun subscribeViewModel(viewModel: DetailsVM) = with(viewModel) {

    }

    private val showPermissionLDObserver = Observer<Unit> {
        requireActivity().checkPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            { viewModel.onPermissionSucceed(navArgs.bookId) },
            { viewModel.onPermissionDenied() }
        )
    }

    private val bookDataLDObserver = Observer<BookData> { bookData ->
        Timber.d("bookDataLDObserver")
        viewBinding.apply {
            Glide
                .with(imageBook)
                .load(bookData.image)
                .into(imageBook)
            textToolbar.text = bookData.name
            textBookName.text = bookData.name
            textBookAuthor.text = bookData.author
            textDetails.text = bookData.details
            if (bookData.pagesCount > 0) {
                progressBook.visibility = VISIBLE
                textBookPages.visibility = VISIBLE
                textBookPages.text = resources.getString(
                    R.string.text_book_pages,
                    bookData.currentPage + 1,
                    bookData.pagesCount
                )
                progressBook.max = bookData.pagesCount-1
                progressBook.progress = bookData.currentPage
            } else {
                progressBook.visibility = GONE
                textBookPages.visibility = GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume(navArgs.bookId)
    }

}