package com.example.screen_recorder;
import com.hbisoft.hbrecorder.HBRecorder;
import com.hbisoft.hbrecorder.HBRecorderCodecInfo;
import com.hbisoft.hbrecorder.HBRecorderListener;
import androidx.annotation.NonNull;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

public class MainActivity extends AppCompatActivity implements HBRecorderListener{
  public HBRecorder hbRecorder;
  private static final int SCREEN_RECORD_REQUEST_CODE = 777;
  private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
  private static final int PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE = PERMISSION_REQ_ID_RECORD_AUDIO + 1;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);     

      //Init HBRecorder
      hbRecorder = new HBRecorder(this, this);   
    }
  @Override
public void HBRecorderOnComplete() {
    //This is called once the file was created
}
@Override
public void HBRecorderOnError(int errorCode) {
    //This is called when an error occurs
}
public void startRecordingScreen() {
  MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
  Intent permissionIntent = mediaProjectionManager != null ? mediaProjectionManager.createScreenCaptureIntent() : null;
  startActivityForResult(permissionIntent, SCREEN_RECORD_REQUEST_CODE);
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  super.onActivityResult(requestCode, resultCode, data);
  if (requestCode == SCREEN_RECORD_REQUEST_CODE) {
      if (resultCode == RESULT_OK) {
          //Start screen recording
          hbRecorder.startScreenRecording(data, resultCode, this);

      }
  }
}
}
/** ScreenRecorderPlugin */
public class ScreenRecorderPlugin implements FlutterPlugin, MethodCallHandler {
  MainActivity x;
  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    final MethodChannel channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "screen_recorder");
    channel.setMethodCallHandler(new ScreenRecorderPlugin());
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "screen_recorder");
    channel.setMethodCallHandler(new ScreenRecorderPlugin());
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("startScreenRecording")) {
      result.success("Recording started Successfully");
      x.startRecordingScreen();
    } else {
      result.success("Recording stopped successfully");
      x.hbRecorder.stopRecrodingScreen();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
  }
}
