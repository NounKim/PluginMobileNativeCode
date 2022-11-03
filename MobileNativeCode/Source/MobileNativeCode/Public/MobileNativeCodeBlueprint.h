#pragma once

#include <Kismet/BlueprintFunctionLibrary.h>
#include <Runtime/Launch/Resources/Version.h>
#include <Async/Async.h>
#include <Engine.h>

#include "NativeUI/Enums/ToastLengthMessage.h"

#include "MobileNativeCodeBlueprint.generated.h"


// #~~~~~~~~~~~~~~~~~~~~~~~~~ begin 2 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//-- Dispatcher
DECLARE_DYNAMIC_DELEGATE_OneParam(FTypeDispacth, const FString&, ReturnValue); // DispatchName, ParamType, ParamName  



//~~~~~~~~~~~~~~~~~~~~~~~~~~~ end 2 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


UCLASS()
class MOBILENATIVECODE_API UMobileNativeCodeBlueprint : public UBlueprintFunctionLibrary
{
  GENERATED_BODY()

public:
  UMobileNativeCodeBlueprint(const FObjectInitializer& ObjectInitializer) : Super(ObjectInitializer) {};

  // #~~~~~~~~~~~~~~~~~~~~~~~~ begin 2 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  //-- Dispatcher
  
	static FTypeDispacth StaticValueDispatch;
	static void StaticFunctDispatch(const FString& ReturnValue);

#if PLATFORM_IOS
  static void CallBackCppIOS(NSString* sResult);
#endif //PLATFORM_IOS
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~ end 2 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


  /**
   * Concatenation of the platform name from native code
   */
  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
  static FString HelloWorld(FString MyStr = "Hello World");

  /**
   * Asynchronous platform name concatenation from native code
   */
  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
  static void asyncHelloWorld(const FTypeDispacth& CallBackPlatform, FString MyStr = "async Hello World");

  /**
   * Displaying a pop-up message
   */
  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
  static void ShowToastMobile(FString Message, EToastLengthMessage Length);

  /**
   * Example of passing different types of arrays and returning a String array with two values
   */
  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
  static void ExampleArray(FString& Arr1, FString& Arr2);

  /**
   * Returns information about the device
   */
  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
  static FString GetDeviceInfo();

  /**
   * Only for Android. Example of working with Java objects inside C++
   */
  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
  static void ExampleMyJavaObject(FString& JavaBundle);



  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
	  static void OpenGallery();

  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
	  static void OpenGallery_CallBack_Size(int return_size);

  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
	  static void DispatchGetter(const FTypeDispacth& CallBackPlatform);


  /*
   * x25519 시크릿키를 생성합니다. 
   */
  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
	  static void CreateSecretKey();

	/*
	 * 퍼블릭 키를 생성합니다. 먼저 시크릿키가 생성된 이후여야 합니다.
	 */
  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
	  static void CreatePublicKey();

	/*
	 * Shared Key를 생성합니다. 이미 시크릿키를 생성한 상태이고, 더불어 상대방의 퍼블릭키를 전달받은 후에 생성하여야 합니다.
	 */
  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
	  static void CreateSharedKey();


  /*
전달받은 상대의 암호키를 Byte Array로 저장하십시오.
이때, 이름은 반드시 OpponentKey 여야 합니다. asyncAndroidFunctionLibClass.java - CreateSharedcKey 참고.
 저장할 경로는 해당 프로젝트의 캐시 폴더
*/
  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
	  static void SaveOpponentKey(TArray<uint8> OpponentKey);

  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
	  static void  Vibrate_During_Duration(int duration);

  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
	  static void  Vibrate_Keep_Going();

  UFUNCTION(BlueprintCallable, Category = "MobileNativeCode Category")
	  static void  Vibrate_Cancel();
};
