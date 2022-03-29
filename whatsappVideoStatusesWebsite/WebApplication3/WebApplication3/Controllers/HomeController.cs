using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using WebApplication3.Models;
using WebApplication3.Viewmodels;

using System.Net.Http;
using System.Threading.Tasks;
using System.Net;
using Newtonsoft.Json;
using System.Text;

namespace WebApplication3.Controllers
{
    public class HomeController : Controller
    {

        private static readonly HttpClient client = new HttpClient();


        public ActionResult Index()
        {
            return View();
        }

        public ActionResult About()
        {
            ViewBag.Message = "Your application description page.";

            return View();
        }



        public ActionResult UploadSuccess()
        {
            return View();
        }

        public ActionResult NewVideo()
        {
            return View();
        }

        [HttpPost]
        public async Task<ActionResult> NewVideo(MyVideoViewmodel model)
        {

            if (/*model == null ||*/
                model.VideoUrl == null ||
                model.ImageUrl == null ||
                String.IsNullOrEmpty(model.Category) ||
                !model.Password.Equals("Rr123456"))
            {
                return RedirectToAction("NewVideo", "Home");
            }



            // uploading the image
            if (!Directory.Exists(Server.MapPath("~/images")))
                Directory.CreateDirectory(Server.MapPath("~/images"));

            Guid guid = Guid.NewGuid();
            string pic = System.IO.Path.GetFileName(guid.ToString() + "." + model.ImageUrl.FileName.Split('.').Last());
            string picPath = System.IO.Path.Combine(
                                   Server.MapPath("~/images"), pic);
            // image is uploaded
            model.ImageUrl.SaveAs(picPath);



            // uploading the video
            if (!Directory.Exists(Server.MapPath("~/videos")))
                Directory.CreateDirectory(Server.MapPath("~/videos"));

            guid = Guid.NewGuid();
            string vid = System.IO.Path.GetFileName(guid.ToString() + "." + model.VideoUrl.FileName.Split('.').Last());
            string vidPath = System.IO.Path.Combine(
                                   Server.MapPath("~/videos"), vid);
            // video is uploaded
            model.VideoUrl.SaveAs(vidPath);




            // send request to the server
            var values = new Dictionary<string, string>
            {
                { "title", model.VideoTitle },
                { "category", model.Category },
                { "videourl", vid },
                { "imageurl", pic }
            };

            var content = new FormUrlEncodedContent(values);

            var response = await client.PostAsync("http://www.roseshamia.com/scripts/newvideo.php", content);

            var responseString = await response.Content.ReadAsStringAsync();

            //return Content(responseString);





            return RedirectToAction("UploadSuccess", "Home");

        }



        public ActionResult NewNotification()
        {
            return View();
        }

        [HttpPost]
        public ActionResult NewNotification(NotificationModel model)
        {
            if (model == null ||
                String.IsNullOrEmpty(model.Password) ||
                String.IsNullOrEmpty(model.Title) ||
                String.IsNullOrEmpty(model.Body) ||
                !model.Password.Equals("Rr123456"))
            {
                return RedirectToAction("NewNotification", "Home");
            }


            SendNotification(model);
            return RedirectToAction("UploadSuccess", "Home");
        }




        
        private void SendNotification(string title, string body, int badge)
        {
            string mySenderId = "1056809921916";
            string myServerKey = "AAAA9g7HrXw:APA91bE0XGndC678EAhiHOO3W-12W2QpKMNUyQoJFvVBL1_9DIWyv1Ybw0_x6CfTXmYtOEPouLHu981YK8dYP1y2PWFY1h7_9Xl29mZEsr6Yb_Q1yxJ1zs8RXByPcQuZzBT5kerg9M7aIRECrBJRC60pGQJ4fOjnkw";
            string myLegacyServerKey = "AIzaSyCTdCo9YH3ssz-YKQcRfp7T8vmThlQ8SO8";



            WebRequest tRequest = WebRequest.Create("https://fcm.googleapis.com/fcm/send");
            tRequest.Method = "post";
            //serverKey - Key from Firebase cloud messaging server  
            tRequest.Headers.Add(string.Format("Authorization: key={0}", myLegacyServerKey));
            //Sender Id - From firebase project setting  
            tRequest.Headers.Add(string.Format("Sender: id={0}", mySenderId));
            tRequest.ContentType = "application/json";
            var payload = new
            {
                to = myServerKey,
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
                                string sResponseFromServer = tReader.ReadToEnd();
                                string s = sResponseFromServer;
                                //result.Response = sResponseFromServer;
                            }
                    }
                }
            }
        }

        private void SendNotification(string title, string body)
        {
            SendNotification(title, body, 0);
        }

        private void SendNotification(NotificationModel notificationModel)
        {
            SendNotification(notificationModel.Title, notificationModel.Body, notificationModel.Badge);
        }
    }
}