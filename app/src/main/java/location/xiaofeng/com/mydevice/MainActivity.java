package location.xiaofeng.com.mydevice;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseDeviceActivity {

    StringBuilder str = null;
    Button button;
    DeviceReceiver deviceReceiver;
    DeviceInfo deviceInfo;
    Activity ac;
    DeviceGetter getter;
    TextView textView;

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int tag = msg.what;
            switch (tag){
                case DeviceManager.BATTERY_INFO:
                    Map map = (Map)msg.obj;

                    Integer level = (Integer) map.get("level");
                    Integer temperature = (Integer) map.get("temperature");
                    Log.e("asdf", level + "====" + temperature);
                    str.append("==battery" +"level:"+level+"=temperature:"+temperature);
                case DeviceManager.AvailRamMemory_INFO:
                    Long availRam = Long.parseLong((String) msg.obj);
                    textView.setText("=="+availRam);
                    default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        deviceInfo = getDeviceManager().getDeviceInfoInstance();
        ac = MainActivity.this;
        str = new StringBuilder();
        getter = getDeviceManager().getDeviceGetterInstance(handler);
        deviceReceiver = getDeviceManager().getDeviceReceiverInstance(handler);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.asdf);
        textView = (TextView) findViewById(R.id.textView);
    }

    @Override
    public void viewListener() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getter.getAvailRamMemory(ac);
                //   textView.setText("==="+availRomMemory[0]+"===="+availRomMemory[1]);

            }
        });
    }
}
