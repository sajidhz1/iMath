package com.zone.interactivemath

import android.speech.RecognizerIntent
import android.content.Intent
import android.widget.Toast
import android.content.ActivityNotFoundException
import android.widget.ImageButton
import android.widget.TextView
import android.os.Bundle
import android.app.Activity
import android.speech.RecognitionListener
import android.view.View
import java.util.*
import android.speech.SpeechRecognizer
import android.util.Log
import android.Manifest.permission.RECORD_AUDIO
import android.annotation.SuppressLint
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.widget.EditText
import com.zone.interactivemath.adapters.ChatAppMsgAdapter
import com.zone.interactivemath.model.ChatAppMsgDTO
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.LinearLayout


import ai.api.AIServiceContext;
import ai.api.AIServiceContextBuilder;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import android.speech.tts.TextToSpeech
import com.airbnb.lottie.LottieAnimationView
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.dialogflow.v2beta1.*
import java.io.InputStream
import java.lang.Exception


class ListeningActivity : Activity(), RecognitionListener {

    private val REQUEST_RECORD_PERMISSION = 100
//    private var txtSpeechInput: TextView? = null
    private var btnSpeak: LottieAnimationView? = null
    private val REQ_CODE_SPEECH_INPUT = 100
    private var speech: SpeechRecognizer? = null
    private var recognizerIntent: Intent? = null
    private val LOG_TAG = "VoiceRecognitionActivity"
    private var listeningInput: TextView? = null
    private var keepListening: Boolean = false
    private var msgRecyclerView: RecyclerView? = null
    private val msgDtoList:ArrayList<ChatAppMsgDTO> = ArrayList()
    private val TAG = MainActivity::class.java.simpleName

    private val uuid = UUID.randomUUID().toString()

    // Java V2
    private var sessionsClient: SessionsClient? = null
    private var session: SessionName? = null

    private var questionCount:Int = 1
    private var tts:TextToSpeech? = null
    private var lastIntent: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listening_activity)

        tts = TextToSpeech(getApplicationContext(), object: TextToSpeech.OnInitListener{

            override fun onInit(status: Int) {
                Log.d("TTS", ""+status)
                if(status != TextToSpeech.ERROR) {
                    tts?.setLanguage(Locale.UK)
                }
            }
        })

//        txtSpeechInput = findViewById(R.id.txtSpeechInput) as TextView
        listeningInput = findViewById(R.id.listening_text) as TextView
        btnSpeak = findViewById(R.id.btnSpeak) as LottieAnimationView

        actionBar?.hide()

        speech = SpeechRecognizer.createSpeechRecognizer(this);
//        Log.i(LOG_TAG, "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(this));
        speech?.setRecognitionListener(this);
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent?.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
            "en");
        recognizerIntent?.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent?.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1000)

        btnSpeak!!.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View) {
              //  promptSpeechInput()
                if(!keepListening){
                    keepListening = true
                }else{
                    keepListening = false
                    showMicIcon()
                    speech?.stopListening()
                    listeningInput!!.text = "Tap on mic to speak"
                }

                ActivityCompat.requestPermissions(
                    this@ListeningActivity,
                    arrayOf(RECORD_AUDIO),
                    REQUEST_RECORD_PERMISSION
                )
            }
        })

        // Get RecyclerView object.
        msgRecyclerView = findViewById<View>(R.id.chat_recycler_view) as RecyclerView

        // Set RecyclerView layout manager.
        val linearLayoutManager = LinearLayoutManager(this)
        msgRecyclerView?.layoutManager = linearLayoutManager

        // Create the data adapter with above data list.
        val chatAppMsgAdapter = ChatAppMsgAdapter(msgDtoList)
        // Set data adapter to RecyclerView.
        msgRecyclerView?.adapter = chatAppMsgAdapter

        initV2Chatbot()

        //showMicIcon()
    }

    private fun initV2Chatbot() {
        try {
            val stream:InputStream = getResources().openRawResource(R.raw.test_agent_credentials);
            val credentials:GoogleCredentials = GoogleCredentials.fromStream(stream);
            val projectId:String = (credentials as ServiceAccountCredentials).projectId;

            val settingsBuilder = SessionsSettings.newBuilder()
            val sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings)
            session = SessionName.of(projectId, uuid);
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun callbackV2(response: DetectIntentResponse?) {
        if (response != null) {
            // process aiResponse here

            val botReply = response.queryResult.fulfillmentText
            tts?.speak(botReply, TextToSpeech.QUEUE_FLUSH, null)
            lastIntent = response.queryResult.intent.displayName

            if(lastIntent.equals("Greeting")){
                sendMessagetoBot("initiate question")
            }
            Log.d(TAG, "V2 Bot Reply: $botReply")
            val msgDto = ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, botReply)
            msgDtoList.add(msgDto)
            msgRecyclerView?.adapter?.notifyItemInserted(msgDtoList.size - 1)

            if(keepListening){
                speech?.startListening(recognizerIntent)
                showProgressIcon()
            }

        } else {
            Log.d(TAG, "Bot Reply: Null")
        }
    }


     override fun onPause(){
          if(tts !=null){
              tts?.stop();
              tts?.shutdown();
          }
          super.onPause();
     }


    fun showProgressIcon() {
        btnSpeak?.setAnimation(AnimationConstants.SEARCH_BAR_LISTENING)
        btnSpeak?.playAnimation()
    }

    /**
     * Show Kohls Icon
     */
    fun showKohlsIcon() {
        btnSpeak?.setAnimation(AnimationConstants.USER_SPEAKING_ANIM)
        btnSpeak?.setFrame(0)
    }

    /**
     * Show Mic Icon
     */
    fun showMicIcon() {
        btnSpeak?.setAnimation(AnimationConstants.USER_SPEAKING_ANIM)
        btnSpeak?.playAnimation()
    }

    /**
     * Receiving speech input
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_CODE_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && null != data) {

                    val result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
         //           txtSpeechInput!!.text = result!![0]
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_RECORD_PERMISSION -> if (grantResults.size > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                showProgressIcon()
                speech?.startListening(recognizerIntent)
            } else {
                Toast.makeText(
                    this@ListeningActivity, "Permission Denied!", Toast
                        .LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        speech?.destroy()
    }

    override fun onReadyForSpeech(p0: Bundle?) {
        listeningInput!!.text = "Listening"
    }

    override fun onRmsChanged(p0: Float) {

    }

    override fun onBufferReceived(p0: ByteArray?) {

    }

    override fun onPartialResults(p0: Bundle?) {

    }

    override fun onEvent(p0: Int, p1: Bundle?) {

    }

    override fun onBeginningOfSpeech() {

    }

    override fun onEndOfSpeech() {
        if(!keepListening) {
            listeningInput!!.text = "Tap on mic to speak"
        }
    }

    @SuppressLint("LongLogTag")
    override fun onError(p0: Int) {
        val errorMessage = getErrorText(p0)
        Log.d(LOG_TAG, "FAILED $errorMessage")
    }

    override fun onResults(results: Bundle?) {
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (matches != null) {

            if(keepListening){
                showMicIcon()
                speech?.stopListening()
            }

            val msgDto = ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_SENT, matches[0])
            msgDtoList.add(msgDto)
            msgRecyclerView?.adapter?.notifyItemInserted(msgDtoList.size - 1)


            if(!lastIntent.isNullOrEmpty() && (lastIntent.equals("help") || lastIntent.equals("answer") || lastIntent.equals("solve"))) {
                sendMessagetoBot(matches[0]+" q"+questionCount)
            }
            else{
                sendMessagetoBot(matches[0])
            }
            questionCount++
        }
        if(keepListening){
            showProgressIcon()
            speech?.startListening(recognizerIntent)
        }
    }

    fun sendMessagetoBot(message: String){
        val queryInput =
            QueryInput.newBuilder().setText(TextInput.newBuilder().setText(message).setLanguageCode("en-US")).build()
        Log.d("====message===",queryInput.toString())
        RequestJavaV2Task(this@ListeningActivity, session, sessionsClient, queryInput).execute()
    }

    fun getErrorText(errorCode: Int): String {
        val message: String
        when (errorCode) {
            SpeechRecognizer.ERROR_AUDIO -> message = "Audio recording error"
            SpeechRecognizer.ERROR_CLIENT -> message = "Client side error"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> message = "Insufficient permissions"
            SpeechRecognizer.ERROR_NETWORK -> message = "Network error"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> message = "Network timeout"
            SpeechRecognizer.ERROR_NO_MATCH -> message = "No match"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> message = "RecognitionService busy"
            SpeechRecognizer.ERROR_SERVER -> message = "error from server"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> message = "No speech input"
            else -> message = "Didn't understand, please try again."
        }
        return message
    }
}