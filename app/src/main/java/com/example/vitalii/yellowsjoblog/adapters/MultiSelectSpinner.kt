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
import com.example.vitalii.yellowsjoblog.api.Projects
import com.example.vitalii.yellowsjoblog.api.Users


/**
 * A Spinner view that does not dismiss the dialog displayed when the control is "dropped down"
 * and the user presses it. This allows for the selection of more than one option.
 */
class MultiSelectSpinner: Spinner, OnMultiChoiceClickListener{
    private var _itemsUsers: Array<String>? = null
    private var _itemsProjects:Array<String>? = null
    private var _selectionUsers: BooleanArray? = null
    private var _selectionProjects: BooleanArray? = null
    private var _proxyAdapter: ArrayAdapter<String>
    private var _title:String? = null
    val usersListOfID = ArrayList<Int>()
    private var usersList:List<Users>? = null
    private var projectsPOJOList:List<Projects>? = null
    val projectsListOfID = ArrayList<Int>()


    /**
     * Add items to show in Spinner
     */

    fun addUsers(newList:List<Users>){
        usersList = newList
        val nameOfUsers = ArrayList<String>()
        for(user in newList){
            if (user.fullName!=null){
                nameOfUsers.add(user.fullName!!)
            }
        }
        setUsers(nameOfUsers)
    }

    fun addProjects(newList:List<Projects>){
        projectsPOJOList = newList
        val nameOfProjects = ArrayList<String>()
        for(project in newList){
            nameOfProjects.add(project.name!!)
        }
        setProjects(nameOfProjects)
    }

        /**
         * Returns a list of strings, one for each selected item.
         * @return
         */
    val selectedUsers: List<String>
        get() {
            val selection = LinkedList<String>()
            for (i in _itemsUsers!!.indices) {
                if (_selectionUsers!![i]) {
                    selection.add(_itemsUsers!![i])
                }
            }
            return selection
        }

    val selectedProjects: List<String>
        get() {
            val selection = LinkedList<String>()
            for (i in _itemsProjects!!.indices) {
                if (_selectionProjects!![i]) {
                    selection.add(_itemsProjects!![i])
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
            for (i in _itemsUsers!!.indices) {
                if (_selectionUsers!![i]) {
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
        if (_selectionUsers != null && which < _selectionUsers!!.size) {
            _selectionUsers!![which] = isChecked
            //var select: String? = null
            if (selectedUsers.isNotEmpty()) {
                if (usersList != null) {
                    for (user in usersList!!) {
                        val selectedUser = _itemsUsers!![which]
                        if (user.fullName!= null) {
                            if (selectedUser == user.fullName!!) {
                                val userID: Int = user.id!!
                                if (!usersListOfID.contains(userID)) {
                                    usersListOfID.add(userID)
                                }; else {
                                    usersListOfID.remove(userID)
                                }
                            }
                        }
                    }
                }
            };else{
                usersListOfID.clear()
            }
        }
            if (_selectionProjects != null && which < _selectionProjects!!.size) {
                _selectionProjects!![which] = isChecked
                if(selectedProjects.isNotEmpty()) {
                    if (projectsPOJOList != null) {
                        for (project in this.projectsPOJOList!!) {
                            val selectedProject = _itemsProjects!![which]
                            if (selectedProject == project.name!!) {
                                val projectID = project.id!!
                                if (!projectsListOfID.contains(projectID)) {
                                    projectsListOfID.add(projectID)
                                    setSelection(which)
                                }; else {
                                    projectsListOfID.remove(projectID)
                                }
                            }
                        }
                    }
                };else{
                    projectsListOfID.clear()
                }
            }
//            if (!isAnySelect())
//                select = _title
//            else
//                select = buildSelectedItemString()

//            _proxyAdapter.clear()
//            _proxyAdapter.add(select)
//            setSelection(0)
    }

    fun setTitle(title: String) {
        _title = title
        _proxyAdapter.clear()
        _proxyAdapter.add(title)
        setSelection(0)
    }

    private fun isAnySelect(): Boolean {
        for (b in _selectionUsers!!) {
            if (b) return true
        }
        return false
    }

    /**
     * {@inheritDoc}
     */
    override fun performClick(): Boolean {
        val builder = AlertDialog.Builder(context)
        val builder2 = AlertDialog.Builder(context)

        builder.setMultiChoiceItems(_itemsUsers, _selectionUsers, this)
        builder2.setMultiChoiceItems(_itemsProjects, _selectionProjects, this)
        if(_itemsUsers!=null){
            builder.show()
        }
        if(_itemsProjects!=null){
            builder2.show()
        }
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
//    fun setItems(items: Array<String>) {
//        _items = items
//        _selectionUsers = BooleanArray(_itemsUsers!!.size)
//
//        Arrays.fill(_selection, false)
//    }

    /**
     * Sets the options for this spinner.
     * @param items
     */
    fun setUsers(items: List<String>) {
        _itemsUsers = items.toTypedArray()
        _selectionUsers = BooleanArray(_itemsUsers!!.size)

        Arrays.fill(_selectionUsers, false)
    }
    fun setProjects(items: List<String>) {
        _itemsProjects = items.toTypedArray()
        _selectionProjects = BooleanArray(_itemsProjects!!.size)

        Arrays.fill(_selectionProjects, false)
    }

    /**
     * Sets the selected options based on an array of string.
     * @param selection
     */
//    fun setSelection(selection: Array<String>) {
//        for (sel in selection) {
//            for (j in _items!!.indices) {
//                if (_items!![j] == sel) {
//                    _selection!![j] = true
//                }
//            }
//        }
//    }

    /**
     * Sets the selected options based on a list of string.
     * @param selection
     */
//    fun setSelection(selection: List<String>) {
//        for (sel in selection) {
//            for (j in _items!!.indices) {
//                if (_items!![j] == sel) {
//                    _selection!![j] = true
//                }
//            }
//        }
//    }

    /**
     * Sets the selected options based on an array of positions.
     * @param selectedIndicies
     */
//    fun setSelection(selectedIndicies: IntArray) {
//        for (index in selectedIndicies) {
//            if (index >= 0 && index < _selection!!.size) {
//                _selection!![index] = true
//            } else {
//                throw IllegalArgumentException("Index $index is out of bounds.")
//            }
//        }
//    }

    /**
     * Builds the string for display in the spinner.
     * @return comma-separated list of selected items
     */
//    private fun buildSelectedItemString(): String {
//        val sb = StringBuilder()
//        var foundOne = false
//
//        for (i in _items!!.indices) {
//            if (_selectionUsers!![i]) {
//                if (foundOne) {
//                    sb.append(", ")
//                }
//                foundOne = true
//
//                sb.append(_items!![i])
//            }
//        }
//
//        return sb.toString()
//    }
}
