package com.github.wnebyte.chess.controller

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.github.wnebyte.chess.R
import com.github.wnebyte.chess.model.AudioManager
import com.github.wnebyte.chess.model.Square
import com.github.wnebyte.chess.ui.MyGridLayoutManager
import java.lang.Exception
import java.lang.IllegalStateException

private const val TAG: String = "GridFragment"

/**
 * This abstract fragment makes up the Chessboard used in all of the game activities.
 */
abstract class GridFragment : Fragment()
{
    private lateinit var squareRecyclerView: RecyclerView

    private lateinit var layoutManager: MyGridLayoutManager

    private var adapter: SquareAdapter? = null

    /** shared view model ("owned" by the hosting activity) */
    protected val vm: GridViewModel by viewModels(
            ownerProducer = { requireActivity() }
    )

    protected lateinit var audio: AudioManager

    protected var callbacks: Callbacks? = null

    interface Callbacks {

        /**
         * Callback function to be called when the game has ended.
         */
        fun onGameIsFinished(message: String)
    }

    /**
     * Called when the fragment is first attached.
     * @throws IllegalStateException if the hosting activity does not implement the proper
     * interface.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callbacks = context as Callbacks
        }
        catch (e: Exception) {
            throw IllegalStateException(
                    "Activity needs to implement the GridFragment.Callbacks interface"
            )
        }
    }

    /**
     * Called when the fragment is no longer attached.
     * Releases resources.
     */
    override fun onDetach()
    {
        audio.release()
        callbacks = null
        super.onDetach()
    }

    /**
     * Inflates the fragments view, initialises any lateinit properties, and assigns the
     * LayoutManager, adapter for this instance's RecyclerView.
     * @return the inflated view.
     */
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_grid_layout, container, false)
        squareRecyclerView = view.findViewById(R.id.fragment_recycler_view)
        layoutManager = MyGridLayoutManager(context, vm.reverseLayout)
        squareRecyclerView.layoutManager = layoutManager
        audio = AudioManager(context)
        updateUI()
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /**
     * Initializes the options menu.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_grid_layout, menu)
    }

    /**
     * Initializes this fragments onOptionsItemSelected.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.rotate_grid -> {
                vm.reverseLayout = !vm.reverseLayout
                layoutManager.reverseLayout = vm.reverseLayout
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    /**
     * Updates the UI by reinitializing this fragment's RecyclerView.adapter.
     */
    protected fun updateUI()
    {
        val squares = vm.squares
        adapter = SquareAdapter(squares)
        squareRecyclerView.adapter = adapter
    }

    /**
     * Abstract function which is to implement any post inputEvent processing.
     */
    protected abstract fun finalizeMove(square: Square)

    /**
     * Fragments inner SquareHolder class.
     */
    private inner class SquareHolder(view: View) : RecyclerView.ViewHolder(view),
            View.OnClickListener, View.OnTouchListener, View.OnDragListener
    {
        private lateinit var square: Square
        private val backgroundImage: ImageView = itemView.findViewById(R.id.square_background)
        private val strokeImage: ImageView = itemView.findViewById(R.id.square_stroke)
        private val circleImage: ImageView = itemView.findViewById(R.id.square_circle)
        private val chessPieceImage: ImageView = itemView.findViewById(R.id.square_chessPiece)
        private val rowTextView = itemView.findViewById(R.id.square_row_text) as TextView
        private val colTextView = itemView.findViewById(R.id.square_col_text) as TextView

        init {
            itemView.setOnClickListener(this)
            itemView.setOnTouchListener(this)
            itemView.setOnDragListener(this)
            itemView.isSoundEffectsEnabled = false
        }

        fun bind(square: Square)
        {
            this.square = square

            backgroundImage.setImageResource(square.background)
            if (square.position.x == 1) {
                rowTextView.text = square.position.y.toString()
            }
            if (square.position.y == 1) {
                colTextView.text = square.position.xAsChar().toString()
            }
            strokeImage.setImageResource(square.stroke)
            circleImage.setImageResource(square.circle)
            chessPieceImage.setImageResource(square.chessPiece?.resId ?: 0)
        }

        override fun onClick(v: View?)
        {
            if (vm.isFriendlySquare(square))
            {
                vm.queue(square)
            }
            else if (vm.isLegalSquare(square))
            {
                finalizeMove(square)
            }
            updateUI()
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(v: View, event: MotionEvent): Boolean
        {
            return if (event.action == MotionEvent.ACTION_DOWN &&
                    vm.isFriendlySquare(square))
            {
                vm.queue(square)
                updateUI()
                val data: ClipData = ClipData.newPlainText("ChessPiece", "")
                val shadowBuilder: View.DragShadowBuilder = View.DragShadowBuilder(chessPieceImage)
                // use deprecated function or upgrade to sdk 24?
                view?.startDrag(data, shadowBuilder, v, 0)
                true
            } else {
                false
            }
        }

        override fun onDrag(v: View, event: DragEvent): Boolean
        {
            when (event.action)
            {
                DragEvent.ACTION_DROP ->
                {
                    if (vm.isLegalSquare(square))
                    {
                        finalizeMove(square)
                        return true
                    }
                }

            }
            return true
        }

    }

    /** Fragment's inner SquareAdapter class */
    private inner class SquareAdapter(var squares: List<Square>) : RecyclerView.Adapter<SquareHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SquareHolder {
            val view = layoutInflater.inflate(R.layout.square, parent, false)
            return SquareHolder(view)
        }

        override fun getItemCount(): Int = squares.size

        override fun onBindViewHolder(holder: SquareHolder, position: Int) {
            val square = squares[position]
            holder.bind(square)
        }
    }
}