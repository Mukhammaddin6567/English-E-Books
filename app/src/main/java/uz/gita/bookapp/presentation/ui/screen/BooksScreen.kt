package uz.gita.bookapp.presentation.ui.screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import uz.gita.bookapp.R
import uz.gita.bookapp.data.model.BookData
import uz.gita.bookapp.databinding.ScreenBooksBinding
import uz.gita.bookapp.presentation.ui.adapter.BookAdapter
import uz.gita.bookapp.presentation.ui.dialog.AboutDialog
import uz.gita.bookapp.presentation.ui.dialog.BottomDialog
import uz.gita.bookapp.presentation.viewmodel.books.BooksVM
import uz.gita.bookapp.presentation.viewmodel.books.BooksVMImpl
import uz.gita.bookapp.utils.Events
import uz.gita.bookapp.utils.hideKeyboard
import uz.gita.bookapp.utils.snackErrorMessage

@AndroidEntryPoint
class BooksScreen : Fragment(R.layout.screen_books), SearchView.OnQueryTextListener {

    private val viewBinding by viewBinding(ScreenBooksBinding::bind)
    private val viewModel: BooksVM by viewModels<BooksVMImpl>()
    private val bookAdapter: BookAdapter by lazy { BookAdapter() }

    private val navController: NavController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }

    override fun onCreate(savedInstanceState: Bundle?) = with(viewModel) {
        super.onCreate(savedInstanceState)
        Events.connectionAvailableLD.observe(this@BooksScreen) { viewModel.networkAvailable() }
        navigateDetailsScreenLD.observe(this@BooksScreen) { bookId ->
            navController.navigate(BooksScreenDirections.actionBooksScreenToDetailsScreen(bookId))
        }
        booksListLD.observe(this@BooksScreen, booksListLDObserver)
        stringErrorLD.observe(this@BooksScreen) { snackErrorMessage(it) }
        intErrorLD.observe(this@BooksScreen) { snackErrorMessage(resources.getString(it)) }
        closeSearchLD.observe(this@BooksScreen, closeSearchLDObserver)
        showBottomDialogLD.observe(this@BooksScreen, showBottomDialogLDObserver)
        showShareDialogLD.observe(this@BooksScreen, showShareDialogLDObserver)
        showRateScreenLD.observe(this@BooksScreen, showRateDialogLDObserver)
        showAboutDialogLD.observe(this@BooksScreen, showAboutDialogLDObserver)
        hideKeyboardLD.observe(this@BooksScreen) { hideKeyboard() }
        popBackStackLD.observe(this@BooksScreen) { requireActivity().finish() }
        uiModeStatusLD.observe(this@BooksScreen, uiModeStatusLDObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscribeViewBinding(viewBinding)
        subscribeViewModel(viewModel)
    }

    private fun subscribeViewBinding(viewBinding: ScreenBooksBinding) = with(viewBinding) {
        buttonMore.setOnClickListener { viewModel.onClickMoreButton() }
        searchView.setOnQueryTextListener(this@BooksScreen)
        searchView.setOnCloseListener { viewModel.onClickClose(); return@setOnCloseListener true }
        swipeRefreshLayout.setOnRefreshListener { viewModel.onSwipe() }
        listBooks.adapter = bookAdapter
        listBooks.layoutManager = LinearLayoutManager(requireContext())
        bookAdapter.setOnClickBookItemListener { bookId -> viewModel.onClickBookItem(bookId) }
    }

    private fun subscribeViewModel(viewModel: BooksVM) = with(viewModel) {
        placeholderListStatusLD.observe(viewLifecycleOwner, placeholderListStatusLDObserver)
        placeholderSearchLD.observe(viewLifecycleOwner, placeholderSearchLDObserver)
        refreshStatusLD.observe(viewLifecycleOwner, refreshStatusLDObserver)
    }

    private val placeholderSearchLDObserver = Observer<Boolean> { status ->
        viewBinding.apply {
            when (status) {
                false -> {
                    containerSearchPlaceholder.visibility = GONE
                    listBooks.visibility = VISIBLE
                }
                else -> {
                    listBooks.visibility = GONE
                    containerSearchPlaceholder.visibility = VISIBLE
                }
            }
        }
    }

    private val placeholderListStatusLDObserver = Observer<Boolean> { status ->
        viewBinding.apply {
            when (status) {
                false -> {
                    containerListPlaceholder.visibility = GONE
                    searchView.visibility = VISIBLE
                    swipeRefreshLayout.visibility = VISIBLE
                }
                else -> {
                    swipeRefreshLayout.visibility = GONE
                    searchView.visibility = GONE
                    containerListPlaceholder.visibility = VISIBLE
                }
            }
        }
    }

    private val booksListLDObserver = Observer<List<BookData>> { list ->
        Timber.d("booksListLDObserver: ${list.size}")
        bookAdapter.submitList(list)
    }

    private val refreshStatusLDObserver = Observer<Boolean> { status ->
        Timber.d("refreshStatusLDObserver: $status")
        viewBinding.swipeRefreshLayout.isRefreshing = status
    }

    private val closeSearchLDObserver = Observer<Unit> {
        viewBinding.searchView.apply {
            setQuery("", true)
            clearFocus()
        }
    }

    private val showBottomDialogLDObserver = Observer<Boolean> { status ->
        val dialog = BottomDialog(status)
        dialog.setOnClickUpdateAllBooksListener { viewModel.onClickUpdateButton() }
        dialog.setOnClickShareListener { viewModel.onClickShareButton() }
        dialog.setOnClickRateListener { viewModel.onClickRateButton() }
        dialog.setOnClickUiModeListener { viewModel.onClickUiModeButton() }
        dialog.setOnClickAboutListener { viewModel.onClickAboutButton() }
        dialog.setOnClickQuitAppListener { viewModel.onClickQuitAppButton() }
        dialog.show(childFragmentManager, "BottomDialog")
    }

    private val showShareDialogLDObserver = Observer<Pair<Int, Int>> { details ->
        val appPackageName = requireContext().packageName
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(
            Intent.EXTRA_TEXT,
            resources.getString(R.string.text_share_details, appPackageName)
        )
        intent.type = "text/plain"
        requireContext().startActivity(
            Intent.createChooser(
                intent,
                resources.getString(details.second)
            )
        )
    }

    private val showRateDialogLDObserver = Observer<Int> { uriString ->
        val appPackageName = requireContext().packageName
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(resources.getString(uriString, appPackageName))
        requireContext().startActivity(intent)
    }

    private val showAboutDialogLDObserver = Observer<Unit> {
        val dialog = AboutDialog()
        dialog.show(childFragmentManager, "AboutDialog")
    }

    private val uiModeStatusLDObserver = Observer<Boolean> { status ->
        when (status) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        viewModel.onSearch(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        viewModel.onSearch(newText)
        return true
    }

}