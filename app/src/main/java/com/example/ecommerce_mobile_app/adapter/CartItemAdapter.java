package com.example.ecommerce_mobile_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce_mobile_app.R;
import com.example.ecommerce_mobile_app.databinding.ListItemCartBinding;
import com.example.ecommerce_mobile_app.model.CartItem;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {
    private List<CartItem> mListCartItems;
    private ObservableField<String> total;
    public void setmListCartItems(List<CartItem> mListCartItems){
        this.mListCartItems = mListCartItems;
        calTotal();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemCartBinding listItemCartBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_cart,parent,false);
        return new CartItemViewHolder(listItemCartBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem cartItem = mListCartItems.get(position);
        holder.listItemCartBinding.setCartItem(cartItem);
    }

    @Override
    public int getItemCount() {
        return mListCartItems != null ? mListCartItems.size() : 0;
    }
    public void calTotal(){
        Float sumPrice = 0f;
        for (CartItem cartItem : mListCartItems){
            sumPrice = sumPrice + cartItem.getSubtotal();
        }
        this.total.set(String.valueOf(sumPrice));
    }
    public class CartItemViewHolder extends RecyclerView.ViewHolder{
        ListItemCartBinding listItemCartBinding;
        public CartItemViewHolder(@NonNull ListItemCartBinding listItemCartBinding) {
            super(listItemCartBinding.getRoot());
            this.listItemCartBinding = listItemCartBinding;
        }
    }
}
