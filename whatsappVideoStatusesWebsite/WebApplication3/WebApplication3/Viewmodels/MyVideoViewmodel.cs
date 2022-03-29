using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.IO;
using System.Linq;
using System.Web;

namespace WebApplication3.Viewmodels
{
    public class MyVideoViewmodel
    {
        [DataType(DataType.Password)]
        [Required(ErrorMessage = "كول خرا ودخل كلمة السر")]
        [Display(Name ="Password")]
        public string Password { get; set; }

        [DataType(DataType.Text)]
        [Display(Name = "Title")]
        [Required(ErrorMessage = "أدخل عنوان الفيديو من فضلك إنه مطلوب")]
        public string VideoTitle { get; set; }



        [Required(ErrorMessage = "أختر التصنيف من فضلك إنه مطلوب")]
        [Display(Name = "Category")]
        public string Category { get; set; }


        [DataType(DataType.Upload)]
        [Required(ErrorMessage = "أدخل فيديو من فضلك إنه مطلوب")]
        [Display(Name = "Video")]
        public HttpPostedFileBase VideoUrl { get; set; }


        [DataType(DataType.Upload)]
        [Required(ErrorMessage = "أدخل صورة الفيديو من فضلك إنها مطلوبة")]
        [Display(Name = "Image")]
        public HttpPostedFileBase ImageUrl { get; set; }

        
    }
}