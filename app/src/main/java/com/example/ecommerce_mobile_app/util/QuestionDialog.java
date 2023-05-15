package com.example.ecommerce_mobile_app.util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_mobile_app.R;
import com.example.ecommerce_mobile_app.adapter.QuestionAdapter;
import com.example.ecommerce_mobile_app.api.RetrofitClient;
import com.example.ecommerce_mobile_app.model.BaseResponse;
import com.example.ecommerce_mobile_app.model.Question;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionDialog extends DialogFragment {
    View view;
    private RecyclerView rcv_question;
    private QuestionAdapter adapter = new QuestionAdapter();
    private List<Question> questions;
    private TextView tv_title;

    public QuestionDialog(List<Question> questions) {
        this.questions = questions;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_show_question_product,container,false);
        viewBinding();
        if (adapter.getQuestions().size() == 0)
        {
            dismiss();
            CustomToast.showFailMessage(getContext(),"This product has no question!");
        }
        return view;
    }
    public void viewBinding(){
        rcv_question = view.findViewById(R.id.rcv_question);
        rcv_question.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setQuestions(questions);
        rcv_question.setAdapter(adapter);
        tv_title = view.findViewById(R.id.tvTitleQuestionLayout);
    }

}
