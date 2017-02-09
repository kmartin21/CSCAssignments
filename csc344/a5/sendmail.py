from __future__ import with_statement
import smtplib
from email import Encoders
from email.mime.base import MIMEBase
from email.mime.multipart import MIMEMultipart
import zipfile
import os


def send_file_zipped(email):
    #these folders were retrieved dynamically
    folders_to_zip = ["/Users/keithmartin/Documents/csc344"]
    #loop through all the folders to zip
    for folder in folders_to_zip:
    #concatenate the folders for file name
      zipfilename = "%s.zip" % (folder.replace("/Users/keithmartin/Documents/csc344", "344"))
      zfile = zipfile.ZipFile(os.path.join(folder, zipfilename), 'w', zipfile.ZIP_DEFLATED)
      #rootlen => zipped files don't have a deep file tree
      rootlen = len(folder) + 1
      for base, dirs, files in os.walk(folder):
          for f in files:
                if f.endswith(".zip"):
                    continue
                if f.endswith(".pyc"):
                    continue
                fn = os.path.join(base, f)
                zfile.write(fn, fn[rootlen:])
      zfile.close()

    attach = '/Users/keithmartin/Documents/csc344/344.zip'
    themsg = MIMEMultipart()
    themsg['Subject'] = 'csc344 Zip File'
    themsg['To'] = email
    themsg['From'] = 'kmartin5@oswego.edu'
    part = MIMEBase('application', "octet-stream")
    part.set_payload(open(attach,"rb").read())
    Encoders.encode_base64(part)
    part.add_header('Content-Disposition', 'attachment; filename="%s"' % os.path.basename(attach))
    themsg.attach(part)
    themsg = themsg.as_string()

    # send the message
    username = 'kmartin5@oswego.edu'
    password = 'boxing21'
    server = smtplib.SMTP('smtp.gmail.com:587')
    server.ehlo()
    server.starttls()
    server.login(username,password)
    server.sendmail('kmartin5@oswego.edu', email, themsg)
    server.quit()
