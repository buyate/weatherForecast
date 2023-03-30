package com.wpl.weatherforecast;

import androidx.fragment.app.Fragment;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/*
*1.声明框架
* 2.执行网络请求操作
 */
public class BaseFragment extends Fragment implements Callback.CommonCallback<String> {

    public void loadDate(String path){
        RequestParams params = new RequestParams(path);
        x.http().get(params, this);
    }

    //获取数据成功时
    @Override
    public void onSuccess(String result) {

    }

    //获取数据失败时
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }

    //取消请求
    @Override
    public void onCancelled(CancelledException cex) {

    }

    //请求完成
    @Override
    public void onFinished() {

    }
}
