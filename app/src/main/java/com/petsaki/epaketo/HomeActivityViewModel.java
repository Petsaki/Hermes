package com.petsaki.epaketo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeActivityViewModel extends ViewModel{
    private MutableLiveData<String> mText;
    private int number = 0;
    private MutableLiveData<Integer> mNumber=new MutableLiveData<>();
    private MutableLiveData<Integer> recyler_main_Y=new MutableLiveData<>();

    public HomeActivityViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
        //mNumber=new MutableLiveData<>();
    }


    public void addNumber(){
        number++;
        //mNumber.getValue();
        //String magic = String.valueOf(mNumber);
        //int magic2=Integer.valueOf(String.valueOf(mNumber));
        //int num = magic2 + 1 ;
        mNumber.setValue(number);
    }

    public void setRecyler_main_Y(int y){
        recyler_main_Y.setValue(y);
    }
    public LiveData<Integer> getRecyler_main_Y(){ return recyler_main_Y;}

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Integer> getNumber() {
        return mNumber;
    }

    public void setmNumber(int numberx) {
        mNumber.setValue(numberx);
    }
}
