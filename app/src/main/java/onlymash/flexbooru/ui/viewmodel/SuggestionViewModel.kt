/*
 * Copyright (C) 2019. by onlymash <im@fiepi.me>, All rights reserved
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package onlymash.flexbooru.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.*
import onlymash.flexbooru.database.SuggestionManager
import onlymash.flexbooru.entity.Suggestion
import onlymash.flexbooru.extension.ioMain

class SuggestionViewModel : ViewModel() {

    private val _suggestions: MediatorLiveData<MutableList<Suggestion>> = MediatorLiveData()

    fun loadSuggestions(booruUid: Long): LiveData<MutableList<Suggestion>> {
        viewModelScope.launch {
            val data = withContext(Dispatchers.IO) {
                SuggestionManager.getSuggestionsByBooruUidLiveData(booruUid)
            }
            _suggestions.addSource(data) {
                _suggestions.postValue(it ?: mutableListOf())
            }
        }
        return _suggestions
    }

    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}