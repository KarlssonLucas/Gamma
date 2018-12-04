import {
    DigitLayout,
    DigitTranslations,
    DigitIfElseRendering
} from "@cthit/react-digit-components";
import React from "react";
import UserForm from "../common-views/user-form/UserForm.view";
import translations from "./EditUserDetails.screen.translations.json";

class EditUserDetails extends React.Component {
    componentDidMount() {
        this.props.websitesLoad();
    }

    render() {
        const { user, usersChange, websites } = this.props;
        return (
            <DigitIfElseRendering
                test={user != null && websites != null}
                ifRender={() => (
                    <DigitTranslations
                        translations={translations}
                        uniquePath="Users.Screen.EditUserDetails"
                        render={text => (
                            <DigitLayout.Center>
                                <UserForm
                                    availableWebsites={websites}
                                    titleText={text.EditUser}
                                    submitText={text.SaveUser}
                                    initialValues={{
                                        ...user,
                                        websites:
                                            user.websites == null
                                                ? []
                                                : user.websites,
                                        acceptanceYear: user.acceptanceYear + ""
                                    }}
                                    onSubmit={(values, actions) => {
                                        usersChange(values, user.cid).then(
                                            response => {
                                                console.log("Save");
                                            }
                                        );
                                    }}
                                />
                            </DigitLayout.Center>
                        )}
                    />
                )}
            />
        );
    }
}
export default EditUserDetails;