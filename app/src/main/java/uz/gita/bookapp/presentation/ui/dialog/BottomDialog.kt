package uz.gita.bookapp.presentation.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber
import uz.gita.bookapp.R
import uz.gita.bookapp.databinding.DialogBottomBinding

class BottomDialog(private val uiModeStatus: Boolean) : BottomSheetDialogFragment() {

    private val viewBinding by viewBinding(DialogBottomBinding::bind)
    private var onCLickUpdateAllBooksListener: (() -> Unit)? = null
    private var onClickShareListener: (() -> Unit)? = null
    private var onClickRateListener: (() -> Unit)? = null
    private var onClickUiModeListener: (() -> Unit)? = null
    private var onClickAboutListener: (() -> Unit)? = null
    private var onClickQuitAppListener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_bottom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(viewBinding) {
        buttonUpdate.setOnClickListener {
            onCLickUpdateAllBooksListener?.invoke()
            dismiss()
        }
        buttonShare.setOnClickListener {
            onClickShareListener?.invoke()
            dismiss()
        }
        buttonRate.setOnClickListener {
            onClickRateListener?.invoke()
            dismiss()
        }
        /*buttonUiMode.apply {
            text = when (uiModeStatus) {
                false -> resources.getString(R.string.text_dark_mode)
                else -> resources.getString(R.string.text_light_mode)
            }
            setOnClickListener {
                onClickUiModeListener?.invoke()
                dismiss()
            }
        }*/
        buttonAbout.setOnClickListener {
            onClickAboutListener?.invoke()
            dismiss()
        }
        buttonQuit.setOnClickListener {
            onClickQuitAppListener?.invoke()
            dismiss()
        }
    }


    fun setOnClickUpdateAllBooksListener(block: () -> Unit) {
        onCLickUpdateAllBooksListener = block
    }

    fun setOnClickShareListener(block: () -> Unit) {
        onClickShareListener = block
    }

    fun setOnClickRateListener(block: () -> Unit) {
        onClickRateListener = block
    }

    fun setOnClickUiModeListener(block: () -> Unit) {
        onClickUiModeListener = block
    }

    fun setOnClickAboutListener(block: () -> Unit) {
        onClickAboutListener = block
    }

    fun setOnClickQuitAppListener(block: () -> Unit) {
        Timber.d("setOnClickQuitAppListener: clicked")
        onClickQuitAppListener = block
    }
}