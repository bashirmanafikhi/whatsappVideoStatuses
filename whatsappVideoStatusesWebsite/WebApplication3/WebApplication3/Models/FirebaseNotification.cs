using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

using System.Net.Http;
using System.Text;
using Newtonsoft.Json;
using System.Net;
using System.IO;

namespace WebApplication3.Models
{
    public class FirebaseNotification
    {
        //public class FirebaseNotificationModel
        //{
        //    [JsonProperty(PropertyName = "to")]
        //    public string To { get; set; }

        //    [JsonProperty(PropertyName = "notification")]
        //    public NotificationModel Notification { get; set; }
        //}

        //public static async void Send(FirebaseNotificationModel firebaseModel)
        //{
        //    HttpRequestMessage httpRequest = null;
        //    HttpClient httpClient = null;

        //    var authorizationKey = string.Format("key={0}", "YourFirebaseServerKey");
        //    var jsonBody = SerializationHelper.SerializeObject(firebaseModel);

        //    try
        //    {
        //        httpRequest = new HttpRequestMessage(HttpMethod.Post, "https://fcm.googleapis.com/fcm/send");

        //        httpRequest.Headers.TryAddWithoutValidation("Authorization", authorizationKey);
        //        httpRequest.Content = new StringContent(jsonBody, Encoding.UTF8, "application/json");

        //        httpClient = new HttpClient();
        //        using (await httpClient.SendAsync(httpRequest))
        //        {
        //        }
        //    }
        //    catch
        //    {
        //        throw;
        //    }
        //    finally
        //    {
        //        httpRequest.Dispose();
        //        httpClient.Dispose();
        //    }
        //}





        string mySenderId = "1056809921916";
        string myKey = "AAAA9g7HrXw:APA91bE0XGndC678EAhiHOO3W-12W2QpKMNUyQoJFvVBL1_9DIWyv1Ybw0_x6CfTXmYtOEPouLHu981YK8dYP1y2PWFY1h7_9Xl29mZEsr6Yb_Q1yxJ1zs8RXByPcQuZzBT5kerg9M7aIRECrBJRC60pGQJ4fOjnkw";
        public void SendNotification(string title, string body, int badge)
        {
            WebRequest tRequest = WebRequest.Create("https://fcm.googleapis.com/fcm/send");
            tRequest.Method = "post";
            //serverKey - Key from Firebase cloud messaging server  
            tRequest.Headers.Add(string.Format("Authorization: key={0}", myKey));
            //Sender Id - From firebase project setting  
            tRequest.Headers.Add(string.Format("Sender: id={0}", mySenderId));
            tRequest.ContentType = "application/json";
            var payload = new
            {
                to = "myTokenDevice",
                priority = "high",
                content_available = true,
                notification = new
                {
                    body = body,
                    title = title, //should no :  
                    sound = "sound.caf",
                    badge = badge,
                    //icon = "https://png.icons8.com/color/50/000000/key.png"
                }/*,
                data = new
                {
                    codigo = UsuarioID,
                    nombreUsuario = UsuarioNombre
                }*/
            };

            string postbody = JsonConvert.SerializeObject(payload).ToString();
            Byte[] byteArray = Encoding.UTF8.GetBytes(postbody);
            tRequest.ContentLength = byteArray.Length;
            using (Stream dataStream = tRequest.GetRequestStream())
            {
                dataStream.Write(byteArray, 0, byteArray.Length);
                using (WebResponse tResponse = tRequest.GetResponse())
                {
                    using (Stream dataStreamResponse = tResponse.GetResponseStream())
                    {
                        if (dataStreamResponse != null) using (StreamReader tReader = new StreamReader(dataStreamResponse))
                            {
                                String sResponseFromServer = tReader.ReadToEnd();
                                //result.Response = sResponseFromServer;
                            }
                    }
                }
            }
        }

    }

}