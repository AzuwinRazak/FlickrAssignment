package com.azuwinrazak.flickrassignment.android.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job



//Initialization of job
private var job = Job()
// Initialization of scope for the coroutine to run in
var scopeForSaving = CoroutineScope(job + Dispatchers.IO)

var scopeForMain = CoroutineScope(job + Dispatchers.Main)