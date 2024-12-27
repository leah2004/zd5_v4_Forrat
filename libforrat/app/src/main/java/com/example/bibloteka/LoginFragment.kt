package com.example.bibloteka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.annotation.SuppressLint
import android.content.Intent

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
  /*  lateinit var logbutton: Button*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }

    }

    private lateinit var db: AppDatabase

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_login_fragment, container, false)
        db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "user-db").build()

        val roleSpinner: Spinner = view.findViewById(R.id.roleSpinner)
        val roles = arrayOf("Библиотекарь", "Преподаватель", "Студент")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleSpinner.adapter = adapter

        view.findViewById<Button>(R.id.loginButton).setOnClickListener {
            val email = view.findViewById<EditText>(R.id.emailEditText).text.toString()
            val login = view.findViewById<EditText>(R.id.loginEditText).text.toString()
            val password = view.findViewById<EditText>(R.id.passwordEditText).text.toString()
            val role = roleSpinner.selectedItem.toString()
            val logbutton: Button = view.findViewById(R.id.loginButton)

            // Проверка введенных данных
            if (email.isNotEmpty() && login.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    // Проверка на наличие пользователей в базе данных
                    val userCount = db.userDao().getUserCount()
                    if (userCount == 0) {
                        Toast.makeText(requireContext(), "База данных пуста. Пожалуйста, зарегистрируйтесь.", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    val user = db.userDao().getUser(email, login, password, role)
                    if (user != null) {
                        when (role) {
                            "Библиотекарь" -> {
                                logbutton.setOnClickListener {
                                    // Создаем намерение для перехода на NewActivity
                                    val intent = Intent(activity, LibBook::class.java)
                                    startActivity(intent) // Запускаем новую активность
                                }
                            }
                            "Преподаватель" -> {
                                logbutton.setOnClickListener {
                                    // Создаем намерение для перехода на NewActivity
                                    val intent = Intent(activity, TeacherActivity::class.java)
                                    startActivity(intent) // Запускаем новую активность
                                }
                            }
                            "Студент" -> {
                                logbutton.setOnClickListener {
                                    // Создаем намерение для перехода на NewActivity
                                    val intent = Intent(activity, StudentActivity::class.java)
                                    startActivity(intent) // Запускаем новую активность
                                }
                            }
                            else -> {
                                Toast.makeText(requireContext(), "Неизвестная роль", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(requireContext(), "Неверные данные", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            }



        }

        // Обработка нажатия на кнопку "Зарегистрироваться"
        view.findViewById<Button>(R.id.registerButton).setOnClickListener {
            val registerFragment = RegisterFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, registerFragment)
                .addToBackStack(null)
                .commit()
        }



        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}