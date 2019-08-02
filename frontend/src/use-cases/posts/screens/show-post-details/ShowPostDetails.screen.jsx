import {
    DigitButton,
    DigitDesign,
    DigitDisplayData,
    DigitIfElseRendering,
    DigitLayout,
    DigitText,
    DigitTranslations
} from "@cthit/react-digit-components";
import React from "react";
import translations from "./ShowPostDetails.screen.translations.json";
import DisplayPostUsages from "./elements/display-post-usages/DisplayPostUsages.element";

function getPostName(post, activeLanguage) {
    switch (activeLanguage.code.toLowerCase()) {
        case "en":
            return post.en;
        case "sv":
            return post.sv;
        default:
            return post.en + "/" + post.sv;
    }
}

class ShowPostDetails extends React.Component {
    constructor(props) {
        super();
    }

    componentDidMount() {
        const {
            gammaLoadingFinished,
            getPost,
            getPostUsages,
            postId
        } = this.props;

        Promise.all([getPost(postId), getPostUsages(postId)]).then(() => {
            gammaLoadingFinished();
        });
    }

    componentWillUnmount() {
        this.props.gammaLoadingStart();
    }

    render() {
        const {
            post,
            dialogOpen,
            toastOpen,
            redirectTo,
            deletePost
        } = this.props;

        return (
            <DigitIfElseRendering
                test={post != null}
                ifRender={() => (
                    <DigitTranslations
                        translations={translations}
                        render={(text, activeLanguage) => (
                            <DigitLayout.Fill>
                                <DigitLayout.Center>
                                    <DigitDesign.Card
                                        minWidth="300px"
                                        maxWidth="600px"
                                    >
                                        <DigitDesign.CardBody>
                                            <DigitText.Title text="Post" />
                                            <DigitDisplayData
                                                data={post}
                                                keysText={{
                                                    id: text.Id,
                                                    sv: text.Sv,
                                                    en: text.En
                                                }}
                                                keysOrder={["id", "sv", "en"]}
                                            />
                                            <DigitText.Title text="Usages" />
                                            <DisplayPostUsages
                                                usages={post.usages}
                                            />
                                        </DigitDesign.CardBody>
                                        <DigitDesign.CardButtons
                                            reverseDirection
                                        >
                                            <DigitDesign.Link
                                                to={
                                                    "/posts/" +
                                                    post.id +
                                                    "/edit"
                                                }
                                            >
                                                <DigitButton
                                                    text="Redigera"
                                                    primary
                                                    raised
                                                />
                                            </DigitDesign.Link>
                                            <DigitLayout.Spacing />
                                            <DigitButton
                                                text={text.DeletePost}
                                                onClick={() => {
                                                    dialogOpen({
                                                        title:
                                                            text.WouldYouLikeToDelete +
                                                            " " +
                                                            getPostName(
                                                                post,
                                                                activeLanguage
                                                            ),
                                                        confirmButtonText:
                                                            text.DeletePost,
                                                        cancelButtonText:
                                                            text.Cancel,
                                                        onConfirm: () => {
                                                            deletePost(post.id)
                                                                .then(
                                                                    response => {
                                                                        toastOpen(
                                                                            {
                                                                                text:
                                                                                    text.YouHaveDeleted +
                                                                                    " " +
                                                                                    getPostName(
                                                                                        post,
                                                                                        activeLanguage
                                                                                    )
                                                                            }
                                                                        );
                                                                        redirectTo(
                                                                            "/posts"
                                                                        );
                                                                    }
                                                                )
                                                                .catch(
                                                                    error => {
                                                                        toastOpen(
                                                                            {
                                                                                text:
                                                                                    text.SomethingWentWrong
                                                                            }
                                                                        );
                                                                    }
                                                                );
                                                        }
                                                    });
                                                }}
                                            />
                                        </DigitDesign.CardButtons>
                                    </DigitDesign.Card>
                                </DigitLayout.Center>
                            </DigitLayout.Fill>
                        )}
                    />
                )}
            />
        );
    }
}

export default ShowPostDetails;
