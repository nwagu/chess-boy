package com.nwagu.chessboy.sharedmodels.presentation.common

import kotlinx.coroutines.CoroutineDispatcher

expect var mainDispatcher: CoroutineDispatcher
expect val ioDispatcher: CoroutineDispatcher