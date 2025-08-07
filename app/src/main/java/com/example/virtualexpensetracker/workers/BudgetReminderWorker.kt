package com.example.virtualexpensetracker.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.virtualexpensetracker.R

class BudgetReminderWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        sendNotification("Budget Review", "Time to review your weekly budget!")
        return Result.success()
    }

    private fun sendNotification(title: String, message: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "budget_channel",
                "Budget Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, "budget_channel")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()

        notificationManager.notify(1, notification)
    }
}
