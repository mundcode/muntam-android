package com.mundcode.muntam.presentation.screen.exams

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.work.WorkManager
import com.mundcode.designsystem.components.bottomsheets.MTBottomSheets
import com.mundcode.designsystem.components.bottomsheets.option.SubjectOptionBottomSheetContent
import com.mundcode.designsystem.components.buttons.TimeRecordButton
import com.mundcode.designsystem.components.dialogs.alert.AlertDialog
import com.mundcode.designsystem.components.dialogs.textfeild.NameEditorDialog
import com.mundcode.designsystem.components.toast.MTToast
import com.mundcode.designsystem.components.toolbars.MTTitleToolbar
import com.mundcode.designsystem.theme.Gray100
import com.mundcode.designsystem.theme.Gray200
import com.mundcode.muntam.presentation.item.AdmobBanner
import com.mundcode.muntam.presentation.item.ExamEmptyItem
import com.mundcode.muntam.presentation.item.ExamItem
import com.mundcode.muntam.util.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun ExamsScreen(
    viewModel: ExamsViewModel = hiltViewModel(),
    onNavEvent: (String) -> Unit,
    onClickBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        launch {
            viewModel.navigationEvent.collectLatest { route ->
                onNavEvent(route)
            }
        }

        launch {
            viewModel.toast.collectLatest { text ->
                viewModel.toastState.showToast(text)
            }
        }

        launch {
            viewModel.alarmCancelEvent.collectLatest {
                WorkManager.getInstance(context).cancelAllWorkByTag(it)
            }
        }
    }

    Scaffold(
        topBar = {
            MTTitleToolbar(
                title = state.subjectTitle,
                onClickBack = onClickBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (state.exams.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ExamEmptyItem()
                }
            } else {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(state.exams) { item ->
                            Column {
                                ExamItem(
                                    exam = item,
                                    onClick = {
                                        viewModel.onClickExam(item)
                                    },
                                    onClickMore = {
                                        viewModel.onClickExamOption(item)
                                    },
                                    onClickSave = {
                                        viewModel.onClickExamSave(item)
                                    }
                                )
                                Divider(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp),
                                    color = Gray200
                                )
                            }
                        }
                    }

                    MTToast(
                        toastState = viewModel.toastState,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    )
                }
            }

            Box {
                Column {
                    TimeRecordButton(
                        onClick = { viewModel.onClickMakeExamRecordButton() },
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(vertical = 16.dp)
                    )
                }
            }

            AdmobBanner(
                modifier = Modifier
                    .background(Gray100)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            )
        }
    }

    MTBottomSheets(
        show = state.showExamOptionBottomSheet,
        onClickOutSide = {}
    ) {
        SubjectOptionBottomSheetContent(
            onClickClose = viewModel::onCancelDialog,
            onClickDelete = {
                viewModel.onClickDeleteExam()
            },
            onClickModify = {
                viewModel.onClickModifyExamName()
            }
        )
    }

    if (state.showUpdateNameDialog) {
        NameEditorDialog(
            hint = "과목명을 입력해주세요",
            onClickCancel = viewModel::onCancelDialog,
            onClickConfirm = viewModel::onSelectExamName
        )
    }

    if (state.showStartExamDialog) {
        NameEditorDialog(
            title = "시험 시작하기",
            hint = "시험명을 입력해주세요",
            onClickCancel = viewModel::onCancelDialog,
            onClickConfirm = viewModel::onClickStart
        )
    }

    if (state.showDeleteConfirmDialog) {
        AlertDialog(
            title = "시험 삭제하기",
            subtitle = "선택한 시험 리스트를 삭제합니다.\n삭제한 항목은 다시 되돌릴 수 없습니다.",
            confirmText = "삭제하기",
            onClickConfirm = viewModel::onClickConfirmDeleteExam,
            onClickCancel = viewModel::onCancelDialog
        )
    }
}
