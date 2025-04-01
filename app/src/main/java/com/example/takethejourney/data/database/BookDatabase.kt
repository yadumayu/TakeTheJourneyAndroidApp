package com.example.takethejourney.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.takethejourney.data.model.Book
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Book::class], version = 1, exportSchema = false)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    companion object{
        @Volatile
        private var INSTANCE: BookDatabase? = null

        fun getDatabase(context: Context): BookDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    "book_database"
                ).addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
        private suspend fun populateDatabase(bookDao: BookDao) {
            if (bookDao.getBookCount() == 0) {
                val books = listOf(
                    Book(
                        title = "Empire V", author = "Viktor Pelevin",
                        description = "A young man named Roman Aleksandrovich Shtorkin becomes a vampire.This happens when Roman accidentally meets another vampire, Brama, who decided to kill himself after a vampire duel. But before he does, he is obliged to give the other man his tongue – the special essence that makes a person a vampire. With the help of the tongue, a vampire can read the mind of a human or another vampire by tasting their blood; as vampires say, tasting",
                        price = 5.99, image = "empire"
                    ),

                    Book(
                        title = "Hyperion", author = "Dan Simmons",
                        description = "Hyperion is a Hugo Award-winning science fiction novel written in 1989 by author Dan Simmons. It is the first of four books set in the fictional Hyperion universe. Over a thousand years into the future, humankind has conquered the space travel and colonized hundreds of worlds in this arm of the Milky Way. The novel then tells the story of seven individuals of different backgrounds whom are chosen to travel to the Outback planet of Hyperion as pilgrims on the Final Shrike Pilgrimage to the mysterious, ancient Time Tombs wherein lives a creature of extreme power, the Shrike.",
                        price = 4.99, image = "hyperion"
                    ),

                    Book(
                        title = "Hotel", author = "Strugackie",
                        description = "The story “Hotel “At the Dead Climber”” begins exactly like a classic detective story about a group of people. The main character, Inspector Peter Glebski, arrives at a hotel that was recommended to him by a colleague, a specialist in safecrackers. Like, there, in the mountains where this hotel is located, there is excellent air, excellent relaxation, and the owner of the hotel, Alek Snevar, is a great person. And so, this same Peter Glebski, goes to the mountains for the weekend, to a hotel...",
                        price = 5.99, image = "deadalphinist"
                    ),

                    Book(
                        title = "Strawberry thief", author = "Joanne Harris",
                        description = "The Strawberry Thief by Joanne Harris” is a story of love, loss, heartbreak, and unbreakable bonds. This novel is written by Joanne Harris.  This is a gorgeous story about how life doesn’t always work out the way we want it to, but if we’re willing, we can still make it a great life. It is a good compelling story, engaging, and easy to read. This is the perfect holiday novel, you can pick it up and get straight back into the story.",
                        price = 6.99, image = "joanneharris"
                    ),

                    Book(
                        title = "1984", author = "George Orwell",
                        description = "1984, or \"Nineteen Eighty-Four,\" is a 1949 novel by George Orwell. It is a dystopian novel, meaning it takes place in a dehumanized, futuristic society. It tells the story of a man who lives in a totalitarian world with a harsh government and how he plans to rebel against his corrupt government and society. 1984 has also invented several famous Orwellian words such as Newspeak, Big Brother and thoughtpolice.",
                        price = 3.99, image = "georgeorwell"
                    ),

                    Book(
                        title = "Halloweens Party", author = "Agatha Cristie",
                        description = "The novel features her Belgian detective Hercule Poirot and the mystery novelist Ariadne Oliver. The novel focuses on child murder (with its possible sexual motivation), the irresponsibility of teenagers and the crisis in crime and punishment. This book was dedicated to P.G. Wodehouse.",
                        price = 3.99, image = "halloweensparty"
                    ),

                    Book(
                        title = "Master and Margarita", author = "Michael Bulgakov",
                        description = "In his book 'The Master and Margarita', Mikhail Bulgakov describes how the Devil, posing as Professor Woland, and his entourage arrives in Soviet Moscow. Pontius Pilate, who meets Yeshua Ha-Nozri (Jesus Christ), and Margarita, a conflicted writer divided between her love for the Master and her loyalty to Woland, are two characters whose tales are intertwined in the book. The book makes fun of Soviet culture and examines the nature of virtue and evil.",
                        price = 4.99, image = "masterandmargarita"
                    ),

                    Book(
                        title = "Suitcase", author = "Sergei Dovlatov",
                        description = "The words “A Novel” appear on the cover of Sergei Dovlatov’s The Suitcase, but that isn’t an accurate description of the book’s 8 chapters which are bookended by the sections “Foreword” and “Instead of an Afterword.” In each chapter Dovlatov (1941-1990) examines one of the few objects found in his suitcase–the single piece of luggage he took with him when he left the Soviet Union and emigrated to America.",
                        price = 5.99, image = "suitcase"
                    ),

                    Book(
                        title = "One Flew over the Cuckoo's Nest", author = "Ken Kesey",
                        description = "One Flew over the Cuckoo’s Nest is a tragic yet inspirational account of one man’s self-sacrifice in a struggle against hypocrisy and oppression. Set on a ward of a mental hospital in Oregon, the novel depicts characters who could be found in many settings and a conflict between authoritarianism and individualism that is truly universal.",
                        price = 6.99, image = "cuckoosnest"
                    ),

                    Book(
                        title = "Crooked House", author = "Agatha Cristie",
                        description = "Charles Hayward returns to England from European war with the hope to marry Sophia Leonides. His plans are spoiled when hears that her wealthy grandfather Aristide Leonides has been murdered, by his own family, no less. Sophia asks him to help her finding out the culprit and announces that she cannot marry him until this is solved. She also indicates that ‘it would be easier if it was the right person.’ His father, Assistant Commissioner of the Yard, gives his assent and encourages the idea.",
                        price = 3.99, image = "crookedhouse"
                    )
                )
                bookDao.insertBooks(books)
            }
    }
    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database -> CoroutineScope(Dispatchers.IO).launch {
                populateDatabase(database.bookDao())
            } }
        }
    }

    }
}