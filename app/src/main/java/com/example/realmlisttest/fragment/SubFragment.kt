package com.example.realmlisttest.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.realmlisttest.database.Address
import com.example.realmlisttest.database.AddressBook
import com.example.realmlisttest.databinding.FragmentSubBinding
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where

class SubFragment : Fragment() {
    private var _binding : FragmentSubBinding? = null
    private val binding get() = _binding!!
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSubBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         binding.createA.setOnClickListener{
             realm.executeTransaction { db: Realm ->
                 val maxId = db.where<Address>().max("id")
                 val nextId = (maxId?.toLong() ?: 0L) + 1L
                 val address = db.createObject<Address>(nextId)
                 address.address = "A"
                 binding.textView.setText("Aを登録しました\n")
             }
         }

        binding.createB.setOnClickListener{
            realm.executeTransaction { db: Realm ->
                val maxId = db.where<Address>().max("id")
                val nextId = (maxId?.toLong() ?: 0L) + 1L
                val address = db.createObject<Address>(nextId)
                address.address = "B"
                binding.textView.setText("Bを登録しました\n")
            }
        }

        binding.select.setOnClickListener{
            realm.executeTransaction{db:Realm->
                val addresses = db.where<Address>().findAll()
                var text:String = "読込しました"
                for(address in addresses){
                    text = text+"&"+address.address
                }
                binding.textView.setText(text)
            }
        }

        binding.delete.setOnClickListener{
            realm.executeTransaction{db:Realm ->
                db.where<Address>()
                    .findFirst()
                    ?.deleteFromRealm()
            }
            binding.textView.setText("削除しました\n")
        }

        binding.ListCreateA.setOnClickListener {
            realm.executeTransaction{db:Realm->
                val maxId = db.where<AddressBook>().max("id")
                val nextId = (maxId?.toLong() ?: 0L) + 1L
                val addressBook = db.createObject<AddressBook>(nextId)

                addressBook.bookName="${nextId}番"
                val addresses = db.where<Address>().equalTo("address","A").findAll()
                addressBook.list.addAll(addresses)
                binding.textView.setText("${addressBook.bookName}のAリストを登録しました\n")
            }
        }

        binding.ListCreateAll.setOnClickListener {
            realm.executeTransaction{db:Realm->
                val maxId = db.where<AddressBook>().max("id")
                val nextId = (maxId?.toLong() ?: 0L) + 1L
                val addressBook = db.createObject<AddressBook>(nextId)

                addressBook.bookName="${nextId}番"
                val addresses = db.where<Address>().findAll()
                addressBook.list.addAll(addresses)
                binding.textView.setText("${addressBook.bookName}のALLリストを登録しました\n")
            }
        }

        binding.ListSelect.setOnClickListener {
            realm.executeTransaction{db:Realm->
                val addressBooks = db.where<AddressBook>().findAll()
                var text:String = "読込しました"
                for(addressBook in addressBooks){
                    text = text+"&"+addressBook.bookName
                    for(address in addressBook.list){
                        text = text+"&"+address.address
                    }
                    text += "\n"
                }
                binding.textView.setText(text)
            }
        }

        binding.ListDelete.setOnClickListener {
            realm.executeTransaction { db: Realm ->
                val addressBook = db.where<AddressBook>().findFirst()
                if (addressBook != null) {
                    addressBook.list.removeAll(addressBook.list.where().findAll())
                }

                var text: String = "アドレスAを削除しました"
                if (addressBook != null) {
                    text = text + "&" + addressBook.bookName
                    for (address in addressBook.list) {
                        text = text + "&" + address.address
                    }
                }
                binding.textView.setText(text)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}