package com.nwagu.chessboy.sharedmodels.presentation

import kotlinx.coroutines.CoroutineDispatcher

expect var mainDispatcher: CoroutineDispatcher
expect val ioDispatcher: CoroutineDispatcher