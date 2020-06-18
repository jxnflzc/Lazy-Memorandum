package per.jxnflzc.memorandumkotlin.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.*
import per.jxnflzc.memorandumkotlin.BuildConfig
import per.jxnflzc.memorandumkotlin.R
import java.io.IOException
import java.util.concurrent.TimeUnit

class UpdateUtil {
    companion object {
        const val URL = "https://api.bq04.com/apps/latest"
        const val ID = "5eeac6ad23389f4934e55832"
        const val API_TOKEN = "b28614c80f1ec9e189366857b3fada8b"
        const val NOTIFICATION = 0
        const val DOWNLOAD = 1



        fun checkUpdate(context: Context, flag: Int) {
            try {
                val client = OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .build()
                val request = Request.Builder()
                    .url("$URL/$ID?api_token=$API_TOKEN")
                    .get()
                    .build()

                val call = client.newCall(request)
                call.enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Looper.prepare()
                        Toast.makeText(
                            context,
                            R.string.check_version_failed,
                            Toast.LENGTH_SHORT
                        ).show()
                        Looper.loop()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val result = response.body?.string()
                        val data = JsonParser().parse(result).asJsonObject

                        Looper.prepare()
                        when (flag) {
                            NOTIFICATION -> notification(context, data)
                            DOWNLOAD -> download(context, data)
                        }
                        Looper.loop()
                    }
                })
            } catch (e: IOException) {
                Looper.prepare()
                Toast.makeText(context, R.string.check_version_failed, Toast.LENGTH_SHORT)
                    .show()
                Looper.loop()
            }
        }

        private fun notification(context: Context, data: JsonObject){
            val versionShort = data.get("versionShort").asString
            val updateUrl = data.get("update_url").asString
            val changelog = data.get("changelog").asString
            if (versionShort != BuildConfig.VERSION_NAME) {
                val channelId = "19986"
                val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(channelId, context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT)
                    manager.createNotificationChannel(channel)
                }
                val uri = Uri.parse(updateUrl)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                val pi = PendingIntent.getActivity(context, 0, intent, 0)
                val notification = NotificationCompat.Builder(context, channelId)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(context.getString(R.string.find_update_confirm))
                    .setSmallIcon(R.mipmap.icon)
                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.icon))
                    .setContentIntent(pi)
                    .build()
                manager.notify(channelId.toInt(), notification)
            }
        }

        private fun download(context: Context, data: JsonObject) {
            val versionShort = data.get("versionShort").asString
            val updateUrl = data.get("update_url").asString
            val changelog = data.get("changelog").asString
            if (versionShort != BuildConfig.VERSION_NAME) {
                AlertDialog.Builder(context).apply{
                    setTitle(R.string.find_update)
                    setMessage(R.string.find_update_confirm)
                    setPositiveButton(R.string.ok) { _, _ ->
                        val uri = Uri.parse(updateUrl)
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    }
                    setNegativeButton(R.string.cancel, null)
                    create()
                    show()
                }
            } else {
                Toast.makeText(context, R.string.version_is_newest, Toast.LENGTH_SHORT).show()
            }
        }
    }
}