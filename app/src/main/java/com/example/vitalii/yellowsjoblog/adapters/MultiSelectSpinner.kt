package com.example.vitalii.yellowsjoblog.adapters

import java.util.Arrays
import java.util.LinkedList

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import com.example.vitalii.yellowsjoblog.WorkTime.ClockFragment
import android.text.method.TextKeyListener.clear



/**
 * A Spinner view that does not dismiss the dialog displayed when the control is "dropped down"
 * and the user presses it. This allows for the selection of more than one option.
 */
class MultiSelectSpinner: Spinner, OnMultiChoiceClickListener{
    private var _itemsUsers:ArrayList<Users>? = null
    private var _items: Array<String>? = null
    private var _selection: BooleanArray? = null
    private var _proxyAdapter: ArrayAdapter<String>
    private var _title:String? = null
    private val usersListOfID = ArrayList<Int>()
    private var usersList = ArrayList<Users>()
    private var projectsList = ArrayList<Projects>()
    private val projectsListOfID = ArrayList<Int>()


    /**
     * Add items to show in Spinner
     */

    fun addUsers(userList:ArrayList<Users>){
        usersList = userList
        val nameOfUsers = ArrayList<String>()
        for(user in usersList){
            nameOfUsers.add(user.name)
        }
        setItems(nameOfUsers)
    }

    fun addProjects(projectList:ArrayList<Projects>){
        projectsList = projectList
        val nameOfProjects = ArrayList<String>()
        for(project in projectsList){
            nameOfProjects.add(project.name)
        }
        setItems(nameOfProjects)
    }

        /**
         * Returns a list of strings, one for each selected item.
     * @return
     */
    val selectedStrings: List<String>
        get() {
            val selection = LinkedList<String>()
            for (i in _items!!.indices) {
                if (_selection!![i]) {
                    selection.add(_items!![i])
                }
            }
            return selection
        }
    /**
     * Returns a list of positions, one for each selected item.
     * @return
     */
    val selectedIndicies: List<Int>
        get() {
            val selection = LinkedList<Int>()
            for (i in _items!!.indices) {
                if (_selection!![i]) {
                    selection.add(i)
                }
            }
            return selection
        }

    /**
     * Constructor for use when instantiating directly.
     * @param context
     */
    constructor(context: Context) : super(context) {

        _proxyAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item)
        super.setAdapter(_proxyAdapter)
    }

    /**
     * Constructor used by the layout inflater.
     * @param context
     * @param attrs
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        _proxyAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item)
        super.setAdapter(_proxyAdapter)
    }

    /**
     * {@inheritDoc}
     */
    override fun onClick(dialog: DialogInterface, which: Int, isChecked: Boolean) {
        if (_selection != null && which < _selection!!.size) {
            _selection!![which] = isChecked

            var select: String? = null
               if (selectedStrings.isNotEmpty()){
                    for (user in usersList){
                        val selectedUser = _items!![which]
                        if (selectedUser == user.name){
                            val userID:Int = user.id
                            if (!usersListOfID.contains(userID)){
                                println("Add new ID $userID in Array")
                                usersListOfID.add(userID)
                            };else{
                                println("ID $userID is already in Array. Remove.")
                                usersListOfID.remove(userID)
                            }
                        }
                    }
                    for (project in projectsList){
                        val selectedProject = _items!![which]
                        if(selectedProject == project.name){
                            val projectID = project.id
                            if(!projectsListOfID.contains(projectID)){
                                println("Add new project ID $projectID in Array")
                                projectsListOfID.add(projectID)
                            };else{
                            println("ID $projectID is already in Array. Remove.")
                            projectsListOfID.remove(projectID)
                            }
                        }
                    }
                };else{
                    usersListOfID.clear()
                }

            println("Selected strings: $selectedStrings")
            println("Selected users IDs $usersListOfID")


            if (!isAnySelect())
                select = _title
            else
                select = buildSelectedItemString()

            _proxyAdapter.clear()
            _proxyAdapter.add(select)
            setSelection(0)
        } else {
            throw IllegalArgumentException("Argument 'which' is out of bounds.")
        }
    }

    fun setTitle(title: String) {
        _title = title
        _proxyAdapter.clear()
        _proxyAdapter.add(title)
        setSelection(0)
    }

    private fun isAnySelect(): Boolean {
        for (b in _selection!!) {
            if (b) return true
        }
        return false
    }

    /**
     * {@inheritDoc}
     */
    override fun performClick(): Boolean {
        val builder = AlertDialog.Builder(context)
        builder.setMultiChoiceItems(_items, _selection, this)
        builder.show()
        return true
    }

    /**
     * MultiSelectSpinner does not support setting an adapter. This will throw an exception.
     * @param adapter
     */
    override fun setAdapter(adapter: SpinnerAdapter) {
        throw RuntimeException("setAdapter is not supported by MultiSelectSpinner.")
    }

    /**
     * Sets the options for this spinner.
     * @param items
     */
    fun setItems(items: Array<String>) {
        _items = items
        _selection = BooleanArray(_items!!.size)

        Arrays.fill(_selection, false)
    }

    /**
     * Sets the options for this spinner.
     * @param items
     */
    fun setItems(items: List<String>) {
        //addItems()
        _items = items.toTypedArray()
        _selection = BooleanArray(_items!!.size)

        Arrays.fill(_selection, false)
    }

    /**
     * Sets the selected options based on an array of string.
     * @param selection
     */
    fun setSelection(selection: Array<String>) {
        for (sel in selection) {
            for (j in _items!!.indices) {
                if (_items!![j] == sel) {
                    _selection!![j] = true
                }
            }
        }
    }

    /**
     * Sets the selected options based on a list of string.
     * @param selection
     */
    fun setSelection(selection: List<String>) {
        for (sel in selection) {
            for (j in _items!!.indices) {
                if (_items!![j] == sel) {
                    _selection!![j] = true
                }
            }
        }
    }

    /**
     * Sets the selected options based on an array of positions.
     * @param selectedIndicies
     */
    fun setSelection(selectedIndicies: IntArray) {
        for (index in selectedIndicies) {
            if (index >= 0 && index < _selection!!.size) {
                _selection!![index] = true
            } else {
                throw IllegalArgumentException("Index $index is out of bounds.")
            }
        }
    }

    /**
     * Builds the string for display in the spinner.
     * @return comma-separated list of selected items
     */
    private fun buildSelectedItemString(): String {
        val sb = StringBuilder()
        var foundOne = false

        for (i in _items!!.indices) {
            if (_selection!![i]) {
                if (foundOne) {
                    sb.append(", ")
                }
                foundOne = true

                sb.append(_items!![i])
            }
        }

        return sb.toString()
    }
}
