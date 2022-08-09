package uz.gita.bookapp.presentation.ui.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import uz.gita.bookapp.R
import uz.gita.bookapp.data.model.BookData
import uz.gita.bookapp.databinding.ScreenReadBinding
import uz.gita.bookapp.presentation.viewmodel.read.ReadVM
import uz.gita.bookapp.presentation.viewmodel.read.ReadVMImpl
import uz.gita.bookapp.utils.snackErrorMessage
import java.io.File


@AndroidEntryPoint
class ReadScreen : Fragment(R.layout.screen_read) {

    private val viewBinding by viewBinding(ScreenReadBinding::bind)
    private val viewModel: ReadVM by viewModels<ReadVMImpl>()
    private val args: ReadScreenArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.apply {
            viewModel.getBook(args.bookId)
            /*showShareDialogLD.observe(this@ReadScreen, showShareDialogLDObserver)*/
            popBackStackLD.observe(this@ReadScreen) { findNavController().popBackStack() }
            networkErrorLD.observe(this@ReadScreen) { snackErrorMessage(it) }
            localErrorLD.observe(this@ReadScreen) { snackErrorMessage(resources.getString(it)) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscribeViewBinding(viewBinding)
        subscribeViewModel(viewModel)
    }

    private fun subscribeViewBinding(viewBinding: ScreenReadBinding) = with(viewBinding) {
        /*textToolbar.isSelected = true*/
        /*buttonShare.setOnClickListener { viewModel.onClickShare() }*/
        swipeRefreshLayout.setOnRefreshListener { viewModel.onRefresh(args.bookId) }
        buttonBack.setOnClickListener { viewModel.onClickBack() }
    }

    private fun subscribeViewModel(viewModel: ReadVM) = with(viewModel) {
        bookDataLD.observe(viewLifecycleOwner, bookDataLDObserver)
        bookReferenceAndPageLD.observe(viewLifecycleOwner, bookReferenceAndPageLDObserver)
        progressStatusLD.observe(viewLifecycleOwner, progressStatusLDObserver)
    }

    private val bookDataLDObserver = Observer<BookData> { data ->
        viewModel.loadBookByReference(requireContext(), data.reference, data.currentPage)
        viewBinding.apply {
            textToolbar.text = data.name
            progressBook.max = data.pagesCount
        }
    }

    private val bookReferenceAndPageLDObserver = Observer<Pair<String, Int>> { referenceAndPage ->
        Timber.d("bookReferenceAndPageLDObserver: ${referenceAndPage.first}")
        Timber.d("bookReferenceAndPageLDObserver: ${referenceAndPage.second}")
        viewBinding.pdfView.fromFile(File(requireContext().filesDir, referenceAndPage.first))
            .defaultPage(referenceAndPage.second)
            .onLoad { viewModel.dismissProgress() }
            .scrollHandle(DefaultScrollHandle(requireContext()))
            .onPageChange { page, pageCount ->
                Timber.d("bookReferenceAndPageLDObserver: $page")
                viewModel.saveCurrentBook(args.bookId, page, pageCount)
                viewBinding.textBookPages.text =
                    resources.getString(R.string.text_book_pages, page + 1, pageCount)
                viewBinding.progressBook.apply {
                    if (max == 0) max = pageCount - 1
                    progress = page
                }
            }
            .load()
    }

    /*private val showShareDialogLDObserver = Observer<String> { bookReference ->

    }*/

    private val progressStatusLDObserver = Observer<Boolean> { status ->
        viewBinding.swipeRefreshLayout.isRefreshing = status
    }

}