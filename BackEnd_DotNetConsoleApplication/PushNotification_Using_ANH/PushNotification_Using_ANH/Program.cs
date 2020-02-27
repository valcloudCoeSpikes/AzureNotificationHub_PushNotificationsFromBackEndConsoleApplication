using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Azure.NotificationHubs;


namespace PushNotification_Using_ANH
{
    class Program
    {
        static void Main(string[] args)
        {
            SendTemplateNotificationAsync();
            Console.ReadLine();
        }

        private static async void SendTemplateNotificationAsync()
        {
            // Define the notification hub.
            string Hubname = "tripnotification";
            string Connectionstring = "Endpoint=sb://moognotificationhub.servicebus.windows.net/;SharedAccessKeyName=DefaultFullSharedAccessSignature;SharedAccessKey=ukXpfw/GWF//fy6plp+yYdkSE+KYMhImcZ5hA0e2jHw=";
            NotificationHubClient hub = NotificationHubClient.CreateClientFromConnectionString(Connectionstring, Hubname);

            // Apple requires the apns-push-type header for all requests
            var headers = new Dictionary<string, string> { { "apns-push-type", "alert" } };

            // Create an array of breaking news categories.
            var categories = new string[] { "World", "Politics", "Business", "Technology", "Science", "Sports" };

            // Send the notification as a template notification. All template registrations that contain
            // "messageParam" and the proper tags will receive the notifications.
            // This includes APNS, GCM/FCM, WNS, and MPNS template registrations.

            Dictionary<string, string> templateParams = new Dictionary<string, string>();

            foreach (var category in categories)
            {
                templateParams["messageParam"] = "Breaking " + category + " News!";
                await hub.SendTemplateNotificationAsync(templateParams, category);
                Console.WriteLine("sent notification of category :{0}", category);
            }
        }
    }
}
