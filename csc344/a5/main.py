import createsymfile
import sendmail
import createhtml

def main():
    sym = createsymfile
    send = sendmail
    chtml = createhtml

    sym.createsym()
    chtml.create()
    email = raw_input("What is your email? ")

    send.send_file_zipped(email)

if __name__ == "__main__":
    main()