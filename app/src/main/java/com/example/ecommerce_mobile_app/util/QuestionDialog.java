package com.example.ecommerce_mobile_app.util;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

    private EditText ed_content;
    private Button btn_send;

    private int productId;

    public QuestionDialog(int productId) {
        this.productId = productId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_show_question_product,container,false);

        getQuestion();
        viewBinding();

        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendQuestion();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.90), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }

    public void viewBinding(){
        ed_content = view.findViewById(R.id.etQuestion);
        btn_send = view.findViewById(R.id.btnSendQuestion);
        rcv_question = view.findViewById(R.id.rcv_question);
        rcv_question.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setQuestions(questions);
        rcv_question.setAdapter(adapter);
    }
    public void getQuestion(){
        RetrofitClient.getInstance().getQuestons(productId).enqueue(new Callback<BaseResponse<List<Question>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Question>>> call, Response<BaseResponse<List<Question>>> response) {
                if (response.isSuccessful())
                    if (response.body().getResponse_message().equals("Success")){
                        questions = response.body().getData();
                        adapter.setQuestions(questions);
                    }
                    else
                        CustomToast.showFailMessage(getContext(),response.body().getResponse_description());
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Question>>> call, Throwable t) {

            }
        });
    }
    public void sendQuestion(){
        RetrofitClient.getInstance().sendQuestion(new PrefManager(getContext()).getCustomer().getId(),productId,ed_content.getText().toString()).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponse_message().equals("Success")){
                        CustomToast.showSuccessMessage(getContext(),response.body().getResponse_description());
                        ed_content.setText("");
                    }
                    else {
                        CustomToast.showFailMessage(getContext(),response.body().getResponse_description());
                    }
                }
                else {
                    CustomToast.showFailMessage(getContext(),"Sending question is failure!");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {

            }
        });
    }
}
